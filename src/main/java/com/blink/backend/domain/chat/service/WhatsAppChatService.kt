package com.blink.backend.domain.chat.service

import com.blink.backend.controller.chat.dto.SendMessageRequest
import com.blink.backend.domain.chat.model.WhatsAppConversation
import com.blink.backend.domain.chat.model.WhatsAppConversationHistory
import com.blink.backend.domain.model.Clinic

interface WhatsAppChatService {
    fun sendMessageByClinic(clinic: Clinic, messageToSend: SendMessageRequest)
    fun getConversationsByClinic(clinic: Clinic, page: Int, pageSize: Int): List<WhatsAppConversation>
    fun getConversationHistoryByClinicAndNumber(
        clinic: Clinic,
        phoneNumber: String,
        page: Int,
        pageSize: Int
    ): List<WhatsAppConversationHistory>
}