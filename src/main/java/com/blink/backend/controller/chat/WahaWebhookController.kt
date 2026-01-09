package com.blink.backend.controller.chat

import com.blink.backend.controller.message.dto.MessageReceivedRequest
import com.blink.backend.domain.service.WhatsAppService
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/whats-app/webhook")
@Tag(name = "Whats-App Chat Controller", description = "Novas apis de chat do whats-app")
class WahaWebhookController(val whatsAppService: WhatsAppService) {
    @PostMapping("receive-message")
    fun receiveMessage(
        @RequestBody message: MessageReceivedRequest?
    ): ResponseEntity<Unit> {
        whatsAppService.receiveMessage(message)
        return ResponseEntity.ok().build()
    }

    @PostMapping("session-status")
    fun sessionStatusUpdated(
        @RequestBody message: MessageReceivedRequest?
    ): ResponseEntity<Unit> {
        whatsAppService.receiveMessage(message)
        return ResponseEntity.ok().build()
    }
}