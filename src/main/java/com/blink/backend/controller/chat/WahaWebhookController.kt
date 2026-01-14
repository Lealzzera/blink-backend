package com.blink.backend.controller.chat

import com.blink.backend.controller.chat.dto.MessageReceivedWaha
import com.blink.backend.controller.chat.dto.WahaSessionStatusUpdate
import com.blink.backend.domain.chat.service.WahaWebhookService
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/whats-app/webhook")
@Tag(name = "WahaWebhook Controller", description = "APIs para webhook do WAHA")
class WahaWebhookController(
    val wahaWebhookService: WahaWebhookService,
    val objectMapper: ObjectMapper,
    private val logger: Logger = LoggerFactory.getLogger(WahaWebhookController::class.java)
) {
    @PostMapping("receive-message")
    fun receiveMessage(
        @RequestBody request: String
    ): ResponseEntity<Unit> {
        logger.info("Received waha message, session=${request}")
        val messageRequest : MessageReceivedWaha = objectMapper.readValue(request, MessageReceivedWaha::class.java)
        wahaWebhookService.receiveMessage(messageRequest.toDomain())
        return ResponseEntity.ok().build()
    }

    @PostMapping("session-status")
    fun sessionStatusUpdated(
        @RequestBody sessionUpdate: WahaSessionStatusUpdate
    ): ResponseEntity<Unit> {
        logger.info("Waha session status updated, session=${sessionUpdate.session}")
        wahaWebhookService.sessionStatusUpdated(
            session = sessionUpdate.session,
            status = sessionUpdate.payload.status,
            phoneNumber = sessionUpdate.me?.id
        )
        return ResponseEntity.ok().build()
    }
}