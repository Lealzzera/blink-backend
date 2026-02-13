package com.blink.backend.controller.chat

import com.blink.backend.controller.chat.dto.SendMessageRequest
import com.blink.backend.domain.chat.model.WhatsAppConversation
import com.blink.backend.domain.chat.model.WhatsAppConversationHistory
import com.blink.backend.domain.chat.service.WhatsAppChatService
import com.blink.backend.domain.exception.NotFoundException
import com.blink.backend.domain.model.auth.AuthenticatedUser
import com.blink.backend.domain.service.ChatConfigurationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/whats-app/chat")
@Tag(name = "WhatsApp Chat", description = "APIs para gerenciamento de conversas do WhatsApp")
class WhatsAppChatController(
    val whatsAppChatService: WhatsAppChatService,
    val chatConfigurationService: ChatConfigurationService
) {

    @GetMapping("conversations")
    @Operation(summary = "Retorna a lista de conversas mais recentes do whats-app")
    fun getConversations(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestParam(required = false) page: Int = 0,
        @RequestParam(required = false) pageSize: Int = 20
    ): ResponseEntity<List<WhatsAppConversation>> {
        return ResponseEntity.ok(
            whatsAppChatService.getConversationsByClinic(
                clinic = user.clinic.toDomain(),
                page = page,
                pageSize = pageSize
            )
        )
    }

    @GetMapping("{phoneNumber}")
    @Operation(summary = "Retorna o histórico de mensagens de uma conversa")
    fun getChatHistory(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable phoneNumber: String,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") pageSize: Int
    ): ResponseEntity<List<WhatsAppConversationHistory>> {
        return ResponseEntity.ok(
            whatsAppChatService.getConversationHistoryByClinicAndNumber(
                clinic = user.clinic.toDomain(),
                phoneNumber = phoneNumber,
                page = page,
                pageSize = pageSize
            )
        )
    }


    @PostMapping("send-message")
    @Operation(summary = "Envia uma mensagem para um contato")
    fun sendMessage(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestBody messageToSend: SendMessageRequest
    ): ResponseEntity<Unit> {
        whatsAppChatService.sendMessageByClinic(user.clinic.toDomain(), messageToSend)
        return ResponseEntity.ok().build()
    }
    @PutMapping("ai-answer/{phoneNumber}")
    @Operation(summary = "Liga/desliga a resposta automática da IA para um paciente")
    @Throws(NotFoundException::class)
    fun toggleChatAiAnswer(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable phoneNumber: String
    ): ResponseEntity<Boolean> {
        return ResponseEntity.ok(
            chatConfigurationService.toggleAiAnswerMode(
                user.clinic.id,
                phoneNumber
            )
        )
    }
}