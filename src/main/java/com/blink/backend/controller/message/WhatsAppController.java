package com.blink.backend.controller.message;

import com.blink.backend.controller.message.dto.MessageReceivedRequest;
import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.controller.message.dto.WhatsAppStatusDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.domain.model.auth.AuthenticatedUser;
import com.blink.backend.domain.service.WhatsAppService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated(forRemoval = true)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/message/whats-app")
@Tag(name = "Old-chat", description = "Apis desatualizadas de chat")
public class WhatsAppController {
    private final WhatsAppService whatsAppService;

    @GetMapping("qr-code")
    public ResponseEntity<byte[]> getWahaQrCode(@AuthenticationPrincipal AuthenticatedUser user) throws NotFoundException {
        log.info("[V1] getWahaQrCode called - clinicId: {}", user.getClinic().getId());
        return ResponseEntity.ok(whatsAppService.getWhatsAppQrCodeByClinic(user.getClinic().getId()));
    }

    @GetMapping("status")
    public ResponseEntity<WhatsAppStatusDto> getWhatsAppStatus(@AuthenticationPrincipal AuthenticatedUser user) throws NotFoundException {
        log.info("[V1] getWhatsAppStatus called - clinicId: {}", user.getClinic().getId());
        return ResponseEntity.ok(whatsAppService.getWhatsAppStatusByClinicId(user.getClinic().getId()));
    }

    @PostMapping("{clinicId}/disconnect")
    public ResponseEntity<WhatsAppStatusDto> disconnect(@PathVariable Integer clinicId) throws NotFoundException {
        log.info("[V1] disconnect called - clinicId: {}", clinicId);
        return ResponseEntity.ok(whatsAppService.disconnectWhatsAppNumber(clinicId));
    }

    @PostMapping("send-message")
    public ResponseEntity<Void> sendMessage(
            @RequestBody SendMessageRequest sendMessageRequest)
            throws NotFoundException, WhatsAppNotConnectedException, InterruptedException {
        log.info("[V1] sendMessage called - phoneNumber: {}", sendMessageRequest.getPhoneNumber());
        whatsAppService.sendMessage(sendMessageRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("receive-message")
    public ResponseEntity<Void> receiveMessage(
            @RequestBody MessageReceivedRequest message)
            throws NotFoundException {
        log.info("[V1] receiveMessage called - session: {}", message.getSession());
        whatsAppService.receiveMessage(message);
        return ResponseEntity.ok().build();
    }
}

