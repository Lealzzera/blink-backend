package com.blink.backend.domain.clinic.chat.service

import com.blink.backend.controller.message.dto.SendMessageRequest
import com.blink.backend.domain.clinic.chat.model.WhatsAppConversation
import com.blink.backend.domain.clinic.chat.model.WhatsAppConversationHistory
import com.blink.backend.domain.model.Clinic
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class WhatsAppChatServiceWahaImpl : WhatsAppChatService {
    override fun sendMessageByClinic(
        clinic: Clinic,
        sendMessageRequest: SendMessageRequest
    ) {
        TODO("Not yet implemented")
    }

    override fun getConversationsByClinic(
        clinic: Clinic,
        page: Int,
        pageSize: Int
    ): Page<WhatsAppConversation> {
        TODO("Not yet implemented")
    }

    override fun getConversationHistoryByClinicAndNumber(
        clinic: Clinic,
        phoneNumber: String,
        page: Int,
        pageSize: Int
    ): Page<WhatsAppConversationHistory> {
        TODO("Not yet implemented")
    }
}