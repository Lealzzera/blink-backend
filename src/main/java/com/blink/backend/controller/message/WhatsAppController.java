package com.blink.backend.controller.message;

import com.blink.backend.controller.message.dto.MessageReceivedRequest;
import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.controller.message.dto.WhatsAppStatusDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.domain.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("message/whats-app")
public class WhatsAppController {
    private final WhatsAppService whatsAppService;

    @GetMapping("{clinicId}/qr-code")
    public ResponseEntity<byte[]> getWahaQrCode(@PathVariable Integer clinicId) throws NotFoundException {
        return ResponseEntity.ok(whatsAppService.getWhatsAppQrCodeByClinic(clinicId));
    }

    @GetMapping("{clinicId}/status")
    public ResponseEntity<WhatsAppStatusDto> getWhatsAppStatus(@PathVariable Integer clinicId) throws NotFoundException {
        return ResponseEntity.ok(whatsAppService.getWhatsAppStatusByClinicId(clinicId));
    }

    @PostMapping("send-message")
    public ResponseEntity<Void> sendMessage(@RequestBody SendMessageRequest sendMessageRequest)
            throws NotFoundException, WhatsAppNotConnectedException {
        whatsAppService.sendMessage(sendMessageRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("receive-message")
    public ResponseEntity<Void> receiveMessage(@RequestBody MessageReceivedRequest message) throws NotFoundException {
        whatsAppService.receiveMessage(message);
        return ResponseEntity.ok().build();
    }
}
