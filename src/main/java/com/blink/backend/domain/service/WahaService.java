package com.blink.backend.domain.service;

import com.blink.backend.controller.message.dto.ChatHistoryDto;
import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.blink.backend.controller.message.dto.MessageReceivedRequest;
import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.controller.message.dto.WhatsAppStatusDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
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
import com.blink.backend.domain.integration.waha.dto.WahaCustomHeaders;
import com.blink.backend.domain.integration.waha.dto.WahaSessionConfig;
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatusResponse;
import com.blink.backend.domain.integration.waha.dto.WahaWebhooks;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.auth.UserClinic;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.PatientRepository;
import com.blink.backend.persistence.repository.auth.UserClinicRepository;
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.blink.backend.domain.integration.waha.dto.WahaWebhookEventTypes.MESSAGE;
import static com.blink.backend.domain.model.message.WhatsAppStatus.SHUTDOWN;
import static java.util.Objects.isNull;

@Log4j2
@Service
@RequiredArgsConstructor
public class WahaService implements WhatsAppService {
    private final FeignWahaClient wahaClient;
    private final ClinicRepositoryService clinicRepository;
    private final UserClinicRepository userClinicRepository;
    @Value("${waha-webhook-url}")
    private final String wahaWebhookUrl;
    private final String WAHA_RECEIVE_MESSAGE_PATH = "/api/v1/message/whats-app/receive-message";
    private final N8nClient n8nClient;
    private final PatientRepository patientRepository;
    private final AppointmentsRepository appointmentsRepository;
    @Value("${default-ai-answer}")
    private final Boolean defaultAiAnswer;
    private final Integer limit = 20;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${WAHA_API_KEY}")
    private final String wahaApiKey;

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

        sendReceivedMessageToBlinkFe(sender, message, clinic);
        if (!isAiResponseTurnedOn(optionalPatient)) {
            log.info("Automatic response turned off for patient {}", sender);
            return;
        }
        sendReceivedMessageToN8n(sender, message, optionalPatient);

    }

    public List<ChatOverviewDto> getChatsOverview(Integer clinicId, Integer page) throws NotFoundException, WhatsAppNotConnectedException {
        Clinic clinic = clinicRepository.findById(clinicId);
        Integer offset = page * limit;
        try {
            log.info("calling-waha-chat-overview, clinicId={}", clinicId);
            ResponseEntity<List<WahaChatOverviewDto>> response = wahaClient.getOverview(clinic.getWahaSession(), limit, offset);
            if (response.getBody() == null) {
                log.info("null-overview-response, clinicId={}", clinicId);
                return List.of();
            }

            log.info("extracting-waha-chat-overview, clinicId={}", clinicId);
            return response.getBody()
                    .stream()
                    .map(chat -> {
                        Optional<Patient> optionalPatient = patientRepository.findByPhoneNumber(chat.getId());
                        Boolean aiAnswer = optionalPatient.map(Patient::getAiAnswer).orElse(true);
                        String patientName = optionalPatient.map(Patient::getName).orElse(null);

                        return chat.toChatOverviewDto(aiAnswer, patientName);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WhatsAppNotConnectedException();
        }

    }

    public List<ChatHistoryDto> getChatHistory(Integer clinicId, String phoneNumber, Integer page)
            throws NotFoundException, WhatsAppNotConnectedException {
        Clinic clinic = clinicRepository.findById(clinicId);
        Integer offset = limit * page;
        try {
            ResponseEntity<List<WahaChatHistory>> response = wahaClient.getMessages(clinic.getWahaSession(), phoneNumber, limit, offset);

            if (response.getBody() == null) {
                return List.of();
            }

            return response.getBody().stream()
                    .map(WahaChatHistory::toChatHistoryDto)
                    .toList();
        } catch (Exception e) {
            throw new WhatsAppNotConnectedException();
        }
    }

    private void restartWahaSession(String tokenizedName) {
        WahaWebhooks wahaWebhooks = WahaWebhooks.builder()
                .url(wahaWebhookUrl.concat(WAHA_RECEIVE_MESSAGE_PATH))
                .events(List.of(MESSAGE))
                .customHeaders(List.of(WahaCustomHeaders.builder()
                        .name("X-Api-Key")
                        .value(wahaApiKey)
                        .build()))
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

    private void sendReceivedMessageToBlinkFe(String sender, String message, Clinic clinic) {
        try {
            List<UserClinic> userClinics = userClinicRepository.findAllByClinicId(clinic.getId());
            List<String> userEmails = userClinics.stream()
                    .map(userClinic -> userClinic.getUser().getEmail())
                    .filter(Objects::nonNull)
                    .toList();

            if (userEmails.isEmpty()) {
                log.warn("No users found for clinicId: {}. Cannot send websocket message.", clinic.getId());
                return;
            }

            ReceivedMessageBlinkFeRequest payload = ReceivedMessageBlinkFeRequest.builder()
                    .phoneNumber(sender)
                    .message(message)
                    .clinicId(clinic.getId())
                    .build();

            for (String email : userEmails) {
                simpMessagingTemplate.convertAndSendToUser(
                        email,
                        "/notify/message-received",
                        payload);
                log.info("Sent message to user {} for clinic {}", email, clinic.getId());
            }

        } catch (Exception e) {
            log.error("Message not sent to front end, message={}", e.getMessage());
        }
    }

    private boolean isAiResponseTurnedOn(Optional<Patient> patient) {
        return (patient.isPresent() && patient.get().getAiAnswer()) || defaultAiAnswer;
    }
}
