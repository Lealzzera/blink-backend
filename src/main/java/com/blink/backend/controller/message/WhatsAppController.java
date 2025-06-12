package com.blink.backend.controller.message;

import com.blink.backend.domain.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("message/whats-app")
public class WhatsAppController {
    private final WhatsAppService whatsAppService;

    @GetMapping("qr-code")
    public ResponseEntity<byte[]> getWahaQrCode() {
        return ResponseEntity.ok(whatsAppService.getWahaQrCode());
    }
}
