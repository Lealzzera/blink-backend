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
import com.blink.backend.domain.integration.waha.dto.WahaPresenceDto;
import com.blink.backend.domain.integration.waha.dto.WahaSessionConfig;
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatusResponse;
import com.blink.backend.domain.integration.waha.dto.WahaWebhooks;
import com.blink.backend.domain.model.message.WhatsAppStatus;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.auth.UserEntity;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.PatientRepository;
import com.blink.backend.persistence.repository.UserEntityRepository;
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
    private final UserEntityRepository userEntityRepository;
    @Value("${waha-webhook-url}")
    private final String wahaWebhookUrl;
    private final N8nClient n8nClient;
    private final PatientRepository patientRepository;
    private final AppointmentsRepository appointmentsRepository;
    @Value("${default-ai-answer}")
    private final Boolean defaultAiAnswer;
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

        log.info("getting-qr-code for clinic {}", clinicId);
        return wahaClient.getWahaQrCode(clinic.getWahaSession());
    }

    @Async
    @Override
    public void sendMessage(SendMessageRequest sendMessageRequest)
            throws NotFoundException, WhatsAppNotConnectedException, InterruptedException {
        Clinic clinic = clinicRepository.findById(sendMessageRequest.getClinicId());
        if (getWhatsAppStatusByClinic(clinic).isNotConnected()) {
            throw new WhatsAppNotConnectedException();
        }
        Thread.sleep(Duration.of(sendMessageRequest.getWait(), ChronoUnit.SECONDS));
        String chatId = sendMessageRequest.getPhoneNumber().concat("@c.us");
        long typingTime = 70L * sendMessageRequest.getMessage().length();
        WahaPresenceDto presenceDto = WahaPresenceDto.builder()
                .chatId(chatId)
                .session(clinic.getWahaSession())
                .build();
        try {
            wahaClient.sendSeen(presenceDto);
        } catch (Exception e) {
            log.error("Seen not send deactivated, message={}", e.getMessage());
        }
        wahaClient.startTyping(presenceDto);
        Thread.sleep(typingTime);

        wahaClient.sendMessage(SendWahaMessageRequest.builder()
                .session(clinic.getWahaSession())
                .phoneNumber(chatId)
                .text(sendMessageRequest.getMessage())
                .build());
        wahaClient.stopTyping(presenceDto);

        sendReceivedMessageToBlinkFe(sendMessageRequest.getPhoneNumber(),
                sendMessageRequest.getMessage(),
                clinic,
                true);
    }

    @Override
    public void receiveMessage(MessageReceivedRequest messageReceivedRequest) throws NotFoundException {
        String sender = messageReceivedRequest.getPayload().getFrom().replace("@c.us", ""); // a pessoa que mandou a mensagem
        String message = messageReceivedRequest.getPayload().getMessage();
        String session = messageReceivedRequest.getSession();
        Optional<Patient> optionalPatient = patientRepository.findByPhoneNumber(sender);
        Clinic clinic = clinicRepository.findByWahaSession(session);

        sendReceivedMessageToBlinkFe(sender, message, clinic, false);
        if (!isAiResponseTurnedOn(optionalPatient)) {
            log.info("Automatic response turned off for patient {}", sender);
            return;
        }
        sendReceivedMessageToN8n(sender, message, optionalPatient, clinic);

    }

    @Override
    public WhatsAppStatusDto disconnectWhatsAppNumber(Integer clinicId)
            throws NotFoundException {
        Clinic clinic = clinicRepository.findById(clinicId);
        wahaClient.logoutSession(clinic.getWahaSession());
        return WhatsAppStatusDto
                .builder()
                .status(WhatsAppStatus.DISCONNECTED)
                .build();
    }

    public List<ChatOverviewDto> getChatsOverview(Integer clinicId, Integer page, Integer pageSize)
            throws NotFoundException,
            WhatsAppNotConnectedException {
        Clinic clinic = clinicRepository.findById(clinicId);
        Integer offset = page * pageSize;
        try {
            log.info("calling-waha-chat-overview, clinicId={}", clinicId);
            ResponseEntity<List<WahaChatOverviewDto>> response = wahaClient.getOverview(clinic.getWahaSession(), pageSize, offset);
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

    public List<ChatHistoryDto> getChatHistory(Integer clinicId, String phoneNumber, Integer page, Integer pageSize)
            throws NotFoundException, WhatsAppNotConnectedException {
        Clinic clinic = clinicRepository.findById(clinicId);
        Integer offset = pageSize * page;
        try {
            ResponseEntity<List<WahaChatHistory>> response = wahaClient.getMessages(clinic.getWahaSession(), phoneNumber, pageSize, offset);

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
        final String WAHA_RECEIVE_MESSAGE_PATH = "/api/v1/message/whats-app/receive-message";
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
        log.info("restarting-waha-session, tokenizedName={}", tokenizedName);
        wahaClient.deleteWahaSession(tokenizedName);
        wahaClient.createWahaSession(CreateWahaSessionRequest.builder()
                .name(tokenizedName)
                .start(true)
                .config(WahaSessionConfig.builder()
                        .webhooks(List.of(wahaWebhooks))
                        .noweb(noWebConfig)
                        .build())
                .build());
        log.info("waha-session-restarted, tokenizedName={}", tokenizedName);
    }

    private WhatsAppStatusDto getWhatsAppStatusByClinic(Clinic clinic) {
        ResponseEntity<WahaSessionStatusResponse> response = wahaClient.getWahaSessionStatus(clinic.getWahaSession());
        if (HttpStatus.NOT_FOUND.equals(response.getStatusCode()) ||
                isNull(response.getBody())) {
            log.info("null-status-response, clinicId={}", clinic.getId());
            return WhatsAppStatusDto.builder()
                    .status(SHUTDOWN)
                    .build();
        }
        if (response.getBody().getStatus().isShutdown()) {
            log.info("whats-app-shutdown, clinicId={}", clinic.getId());
            return WhatsAppStatusDto.builder()
                    .status(SHUTDOWN)
                    .build();
        }

        WahaSessionStatusResponse responseBody = response.getBody();
        String phoneNumber = null;
        if (responseBody.getStatus().isConnected()) {
            phoneNumber = responseBody.getMe().getPhoneNumber();
        }
        log.info("whats-app-status-response, clinicId={}, status={}", clinic.getId(), responseBody.getStatus());
        return WhatsAppStatusDto.builder()
                .status(responseBody.getStatus().getConnectionStatus())
                .connectedPhoneNumber(phoneNumber)
                .build();
    }

    private void sendReceivedMessageToN8n(String sender, String message, Optional<Patient> patient, Clinic clinic) {

        String patientName = "";
        List<Appointment> appointment = List.of();

        if (patient.isPresent()) {
            patientName = patient.get().getName();
            appointment = appointmentsRepository
                    .findAllByPatientIdAndScheduledTimeAfter(patient.get().getId(), LocalDateTime.now().minusDays(7));
        }
        try {
            n8nClient.receiveMessage(N8nMessageReceived.builder()
                    .sender(sender)
                    .message(message)
                    .senderName(patientName)
                    .appointmentsData(appointment.stream().map(AppointmentsData::fromAppointment).collect(Collectors.toList()))
                    .clinicName(clinic.getClinicName())
                    .clinicId(clinic.getId())
                    .build());
        } catch (Exception e) {
            log.error("N8n responded with an error, e={}", e.getMessage());
        }

    }

    private void sendReceivedMessageToBlinkFe(String sender, String message, Clinic clinic, Boolean fromMe) {
        try {
            List<UserEntity> users = userEntityRepository.findAllByClinicId(clinic.getId());
            List<String> userIds = users.stream()
                    .map(UserEntity::getUserId)
                    .map(UUID::toString)
                    .filter(Objects::nonNull)
                    .toList();

            if (userIds.isEmpty()) {
                log.warn("No users found for clinicId: {}. Cannot send websocket message.", clinic.getId());
                return;
            }

            ReceivedMessageBlinkFeRequest payload = ReceivedMessageBlinkFeRequest.builder()
                    .phoneNumber(sender)
                    .message(message)
                    .clinicId(clinic.getId())
                    .fromMe(fromMe)
                    .build();

            for (String userId : userIds) {
                simpMessagingTemplate.convertAndSendToUser(
                        userId,
                        "/notify/message-received",
                        payload);
                log.info("Sent message to user {} for clinic {}", userId, clinic.getId());
            }

        } catch (Exception e) {
            log.error("Message not sent to front end, message={}", e.getMessage());
        }
    }

    private boolean isAiResponseTurnedOn(Optional<Patient> patient) {
        return (patient.isPresent() && patient.get().getAiAnswer()) || defaultAiAnswer;
    }
}
