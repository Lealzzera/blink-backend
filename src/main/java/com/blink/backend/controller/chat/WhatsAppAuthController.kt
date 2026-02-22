package com.blink.backend.controller.chat

import com.blink.backend.controller.chat.dto.WhatsAppStatusDto
import com.blink.backend.domain.chat.service.WhatsAppAuthService
import com.blink.backend.domain.model.auth.AuthenticatedUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/whats-app/auth")
@Tag(name = "WhatsApp Auth", description = "APIs para autenticação e conexão com WhatsApp")
class WhatsAppAuthController(
    private val whatsAppAuthService: WhatsAppAuthService,
) {

    @GetMapping("qr-code", produces = [MediaType.IMAGE_PNG_VALUE])
    @Operation(summary = "Retorna o QR Code para autenticação do WhatsApp")
    fun getWahaQrCode(@AuthenticationPrincipal user: AuthenticatedUser): ResponseEntity<ByteArray> {
        return ResponseEntity.ok(whatsAppAuthService.getQrCodeByClinic(user.clinic.toDomain()))
    }

    @DeleteMapping
    @Operation(summary = "Desconecta a sessão do WhatsApp")
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
    @Operation(summary = "Retorna o status da conexão do WhatsApp")
    fun getWhatsAppStatus(@AuthenticationPrincipal user: AuthenticatedUser): ResponseEntity<WhatsAppStatusDto> {
        return ResponseEntity.ok(
            WhatsAppStatusDto.fromDomain(
                whatsAppAuthService.getStatusByClinic(user.clinic.toDomain())
            )
        )
    }
}