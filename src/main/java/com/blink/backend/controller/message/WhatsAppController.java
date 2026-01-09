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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated(forRemoval = true)
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/message/whats-app")
@Tag(name = "Old-chat", description = "Apis desatualizadas de chat")
public class WhatsAppController {
    private final WhatsAppService whatsAppService;

    @GetMapping("qr-code")
    public ResponseEntity<byte[]> getWahaQrCode(@AuthenticationPrincipal AuthenticatedUser user) throws NotFoundException {
        return ResponseEntity.ok(whatsAppService.getWhatsAppQrCodeByClinic(user.getClinic().getId()));
    }

    @GetMapping("status")
    public ResponseEntity<WhatsAppStatusDto> getWhatsAppStatus(@AuthenticationPrincipal AuthenticatedUser user) throws NotFoundException {
        return ResponseEntity.ok(whatsAppService.getWhatsAppStatusByClinicId(user.getClinic().getId()));
    }

    @PostMapping("{clinicId}/disconnect")
    public ResponseEntity<WhatsAppStatusDto> disconnect(@PathVariable Integer clinicId) throws NotFoundException {
        return ResponseEntity.ok(whatsAppService.disconnectWhatsAppNumber(clinicId));
    }

    @PostMapping("send-message")
    public ResponseEntity<Void> sendMessage(
            @RequestBody SendMessageRequest sendMessageRequest)
            throws NotFoundException, WhatsAppNotConnectedException, InterruptedException {
        whatsAppService.sendMessage(sendMessageRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("receive-message")
    public ResponseEntity<Void> receiveMessage(
            @RequestBody MessageReceivedRequest message)
            throws NotFoundException {
        whatsAppService.receiveMessage(message);
        return ResponseEntity.ok().build();
    }
}

