package com.blink.backend.domain.service;

import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.controller.message.dto.WhatsAppStatusDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.domain.integration.FeignWahaClient;
import com.blink.backend.domain.integration.waha.dto.CreateWahaSessionRequest;
import com.blink.backend.domain.integration.waha.dto.SendWahaMessageRequest;
import com.blink.backend.domain.integration.waha.dto.WahaSessionConfig;
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatusResponse;
import com.blink.backend.domain.integration.waha.dto.WahaWebhooks;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.blink.backend.domain.integration.waha.dto.WahaWebhookEventTypes.MESSAGE;
import static com.blink.backend.domain.model.message.WhatsAppStatus.SHUTDOWN;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class WahaService implements WhatsAppService {
    private final FeignWahaClient wahaClient;
    private final ClinicRepositoryService clinicRepository;
    @Value("${waha-webhook-url}")
    private final String wahaWebhookUrl;
    private final String WAHA_RECEIVE_MESSAGE_PATH = "/message/whats-app/receive-message";

    @Override
    public WhatsAppStatusDto getWhatsAppStatusByClinicId(Integer clinicId) throws NotFoundException {
        Clinic clinic = clinicRepository.findById(clinicId);
        return getWhatsAppStatusByClinic(clinic);
    }

    @Override
    public byte[] getWhatsAppQrCodeByClinic(Integer clinicId) throws NotFoundException {
        Clinic clinic = clinicRepository.findById(clinicId);

        if (getWhatsAppStatusByClinic(clinic).isShutdown()) {
            restartWahaSession(clinic.getTokenizedName());
        }

        return wahaClient.getWahaQrCode(clinic.getTokenizedName());
    }

    @Override
    public void sendMessage(SendMessageRequest sendMessageRequest) throws NotFoundException, WhatsAppNotConnectedException {
        Clinic clinic = clinicRepository.findById(sendMessageRequest.getClinicId());

        if (getWhatsAppStatusByClinic(clinic).isNotConnected()) {
            throw new WhatsAppNotConnectedException();
        }
        wahaClient.sendMessage(SendWahaMessageRequest.builder()
                .session(clinic.getTokenizedName())
                .phoneNumber(sendMessageRequest.getPhoneNumber().concat("@c.us"))
                .text(sendMessageRequest.getMessage())
                .build());
    }

    private void restartWahaSession(String tokenizedName) {
        WahaWebhooks wahaWebhooks = WahaWebhooks.builder()
                .url(wahaWebhookUrl.concat(WAHA_RECEIVE_MESSAGE_PATH))
                .events(List.of(MESSAGE))
                .build();

        wahaClient.deleteWahaSession(tokenizedName);
        wahaClient.createWahaSession(CreateWahaSessionRequest.builder()
                .name(tokenizedName)
                .start(true)
                .config(WahaSessionConfig.builder()
                        .webhooks(List.of(wahaWebhooks))
                        .build())
                .build());
    }

    private WhatsAppStatusDto getWhatsAppStatusByClinic(Clinic clinic) {
        ResponseEntity<WahaSessionStatusResponse> response = wahaClient.getWahaSessionStatus(clinic.getTokenizedName());
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
}
