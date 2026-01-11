package com.blink.backend.controller.chat

import com.blink.backend.controller.chat.dto.MessageReceivedWaha
import com.blink.backend.controller.chat.dto.WahaSessionStatusUpdate
import com.blink.backend.domain.chat.service.WahaWebhookService
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatus
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
class WahaWebhookController(val wahaWebhookService: WahaWebhookService) {
    @PostMapping("receive-message")
    fun receiveMessage(
        @RequestBody messageRequest: MessageReceivedWaha
    ): ResponseEntity<Unit> {
        wahaWebhookService.receiveMessage(messageRequest.toDomain())
        return ResponseEntity.ok().build()
    }

    @PostMapping("session-status")
    fun sessionStatusUpdated(
        @RequestBody sessionUpdate: WahaSessionStatusUpdate
    ): ResponseEntity<Unit> {
        wahaWebhookService.sessionStatusUpdated(
            session = sessionUpdate.session,
            status = sessionUpdate.payload.status,
            phoneNumber = sessionUpdate.me?.id
        )
        return ResponseEntity.ok().build()
    }
}