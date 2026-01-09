package com.blink.backend.controller.chat

import com.blink.backend.controller.chat.dto.WhatsAppStatusDto
import com.blink.backend.domain.model.auth.AuthenticatedUser
import com.blink.backend.domain.clinic.chat.service.WhatsAppAuthService
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/whats-app/auth")
@Tag(name = "Whats-App Chat Controller", description = "Novas apis de chat do whats-app")
class WhatsAppAuthController(
    private val whatsAppAuthService: WhatsAppAuthService,
) {

    @GetMapping("qr-code", produces = [MediaType.IMAGE_PNG_VALUE])
    fun getWahaQrCode(@AuthenticationPrincipal user: AuthenticatedUser): ResponseEntity<ByteArray> {
        return ResponseEntity.ok(whatsAppAuthService.getQrCodeByClinic(user.clinic.toDomain()))
    }

    @DeleteMapping
    fun disconnect(@AuthenticationPrincipal user: AuthenticatedUser): ResponseEntity<WhatsAppStatusDto> {
        return ResponseEntity.ok(
            WhatsAppStatusDto.fromDomain(
                whatsAppAuthService.disconnectByClinic(
                    user.clinic.toDomain()
                )
            )
        )
    }

    @GetMapping("status")
    fun getWhatsAppStatus(@AuthenticationPrincipal user: AuthenticatedUser): ResponseEntity<WhatsAppStatusDto> {
        return ResponseEntity.ok(
            WhatsAppStatusDto.fromDomain(
                whatsAppAuthService.getStatusByClinic(user.clinic.toDomain())
            )
        )
    }
}