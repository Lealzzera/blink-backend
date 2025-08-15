package com.blink.backend.domain.service;

import com.blink.backend.controller.message.dto.ChatHistoryDto;
import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.blink.backend.controller.message.dto.MessageReceivedRequest;
import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.controller.message.dto.WhatsAppStatusDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.domain.integration.blink.fe.BlinkFeClient;
import com.blink.backend.domain.integration.blink.fe.dto.ReceivedMessageBlinkFeRequest;
import com.blink.backend.domain.integration.n8n.N8nClient;
import com.blink.backend.domain.integration.n8n.dto.AppointmentsData;
import com.blink.backend.domain.integration.n8n.dto.N8nMessageReceived;
import com.blink.backend.domain.integration.waha.FeignWahaClient;
import com.blink.backend.domain.integration.waha.dto.CreateWahaSessionRequest;
import com.blink.backend.domain.integration.waha.dto.NoWebConfig;
import com.blink.backend.domain.integration.waha.dto.SendWahaMessageRequest;
import com.blink.backend.domain.integration.waha.dto.StoreConfig;
import com.blink.backend.domain.integration.waha.dto.WahaChatHistory;
import com.blink.backend.domain.integration.waha.dto.WahaChatOverviewDto;
import com.blink.backend.domain.integration.waha.dto.WahaSessionConfig;
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatusResponse;
import com.blink.backend.domain.integration.waha.dto.WahaWebhooks;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.ChatRepository;
import com.blink.backend.persistence.repository.PatientRepository;
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.blink.backend.domain.integration.waha.dto.WahaWebhookEventTypes.MESSAGE;
import static com.blink.backend.domain.model.message.WhatsAppStatus.SHUTDOWN;
import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class WahaService implements WhatsAppService {
    private final FeignWahaClient wahaClient;
    private final ClinicRepositoryService clinicRepository;
    @Value("${waha-webhook-url}")
    private final String wahaWebhookUrl;
    private final String WAHA_RECEIVE_MESSAGE_PATH = "/api/v1/message/whats-app/receive-message";
    private final N8nClient n8nClient;
    private final PatientRepository patientRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final BlinkFeClient blinkFeClient;
    private final ChatRepository chatRepository;

    @Override
    public WhatsAppStatusDto getWhatsAppStatusByClinicId(Integer clinicId) throws NotFoundException {
        Clinic clinic = clinicRepository.findById(clinicId);
        return getWhatsAppStatusByClinic(clinic);
    }

    @Override
    public byte[] getWhatsAppQrCodeByClinic(Integer clinicId) throws NotFoundException {
        Clinic clinic = clinicRepository.findById(clinicId);

        if (getWhatsAppStatusByClinic(clinic).isShutdown()) {
            restartWahaSession(clinic.getWahaSession());
        }

        return wahaClient.getWahaQrCode(clinic.getWahaSession());
    }

    @Override
    public void sendMessage(SendMessageRequest sendMessageRequest) throws NotFoundException, WhatsAppNotConnectedException {
        Clinic clinic = clinicRepository.findById(sendMessageRequest.getClinicId());

        if (getWhatsAppStatusByClinic(clinic).isNotConnected()) {
            throw new WhatsAppNotConnectedException();
        }
        wahaClient.sendMessage(SendWahaMessageRequest.builder()
                .session(clinic.getWahaSession())
                .phoneNumber(sendMessageRequest.getPhoneNumber().concat("@c.us"))
                .text(sendMessageRequest.getMessage())
                .build());
    }

    @Override
    public void receiveMessage(MessageReceivedRequest messageReceivedRequest) throws NotFoundException {
        String sender = messageReceivedRequest.getPayload().getFrom().replace("@c.us", ""); // a pessoa que mandou a mensagem
        String message = messageReceivedRequest.getPayload().getMessage();
        String session = messageReceivedRequest.getSession();
        Optional<Patient> optionalPatient = patientRepository.findByPhoneNumber(sender);
        Clinic clinic = clinicRepository.findByWahaSession(session);

        sendReceivedMessageToBlinkFe(sender, message, clinic.getId());
        if (!isAiResponseTurnedOn(optionalPatient, clinic.getId())) {
            log.info("Automatic response turned off for patient {}", sender);
            return;
        }
        sendReceivedMessageToN8n(sender, message, optionalPatient);

    }

    public List<ChatOverviewDto> getChatsOverview(Integer clinicId) throws NotFoundException {
        Clinic clinic = clinicRepository.findById(clinicId);

        ResponseEntity<List<WahaChatOverviewDto>> response = wahaClient.getOverview(clinic.getWahaSession());
        if (response.getBody() == null) {
            return List.of();
        }

        return response.getBody()
                .stream()
                .map(chat -> {
                    Optional<Patient> optionalPatient = patientRepository.findByPhoneNumber(chat.getId());
                    Boolean aiAnswer = optionalPatient
                            .map(patient -> chatRepository
                                    .findAiAnswerByPatientIdAndClinicId(patient.getId(), clinicId))
                            .orElse(true);
                    String patientName = optionalPatient.map(Patient::getName).orElse(null);

                    return chat.toChatOverviewDto(aiAnswer, patientName);
                })
                .collect(Collectors.toList());
    }

    public List<ChatHistoryDto> getChatHistory(Integer clinicId, String phoneNumber) throws NotFoundException {
        Clinic clinic = clinicRepository.findById(clinicId);
        ResponseEntity<List<WahaChatHistory>> response = wahaClient.getMessages(clinic.getWahaSession(), phoneNumber/*, 10*/);

        if (response.getBody() == null) {
            return List.of();
        }

        return response.getBody().stream()
                .map(WahaChatHistory::toChatHistoryDto)
                .toList();
    }

    private void restartWahaSession(String tokenizedName) {
        WahaWebhooks wahaWebhooks = WahaWebhooks.builder()
                .url(wahaWebhookUrl.concat(WAHA_RECEIVE_MESSAGE_PATH))
                .events(List.of(MESSAGE))
                .build();
        NoWebConfig noWebConfig = NoWebConfig.builder()
                .store(StoreConfig.builder()
                        .enabled(true)
                        .fullSync(false)
                        .build())
                .build();

        wahaClient.deleteWahaSession(tokenizedName);
        wahaClient.createWahaSession(CreateWahaSessionRequest.builder()
                .name(tokenizedName)
                .start(true)
                .config(WahaSessionConfig.builder()
                        .webhooks(List.of(wahaWebhooks))
                        .noweb(noWebConfig)
                        .build())
                .build());
    }

    private WhatsAppStatusDto getWhatsAppStatusByClinic(Clinic clinic) {
        ResponseEntity<WahaSessionStatusResponse> response = wahaClient.getWahaSessionStatus(clinic.getWahaSession());
        if (HttpStatus.NOT_FOUND.equals(response.getStatusCode()) ||
                isNull(response.getBody()) ||
                response.getBody().getStatus().isShutdown()) {
            return WhatsAppStatusDto.builder()
                    .status(SHUTDOWN)
                    .build();
        }

        WahaSessionStatusResponse responseBody = response.getBody();
        String phoneNumber = null;
        if (responseBody.getStatus().isConnected()) {
            phoneNumber = responseBody.getMe().getPhoneNumber();
        }
        return WhatsAppStatusDto.builder()
                .status(responseBody.getStatus().getConnectionStatus())
                .connectedPhoneNumber(phoneNumber)
                .build();
    }

    private void sendReceivedMessageToN8n(String sender, String message, Optional<Patient> patient) {

        String patientName = "";
        List<Appointment> appointment = List.of();

        if (patient.isPresent()) {
            patientName = patient.get().getName();
            appointment = appointmentsRepository
                    .findAllByPatientIdAndScheduledTimeAfter(patient.get().getId(), LocalDateTime.now().minusDays(7));
        }

        n8nClient.receiveMessage(N8nMessageReceived.builder()
                .sender(sender)
                .message(message)
                .senderName(patientName)
                .appointmentsData(appointment.stream().map(AppointmentsData::fromAppointment).collect(Collectors.toList()))
                .build());
    }

    private void sendReceivedMessageToBlinkFe(String sender, String message, Integer clinicId) {
        try {
            blinkFeClient.sendReceivedMessageToFrontEnd(ReceivedMessageBlinkFeRequest.builder()
                    .sender(sender)
                    .message(message)
                    .clinicId(clinicId)
                    .build());
        } catch (Exception e) {
            log.error("Message not sent to front end, message={}", e.getMessage());
        }
    }

    private boolean isAiResponseTurnedOn(Optional<Patient> patient, Integer clinicId) {
        return patient.isPresent() &&
                chatRepository.findAiAnswerByPatientIdAndClinicId(patient.get().getId(), clinicId);
    }
}
