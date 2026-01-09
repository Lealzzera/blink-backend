package com.blink.backend.domain.clinic.chat.model

import java.time.LocalDateTime

data class WhatsAppConversation(
    val whatsAppName: String,
    val patientName: String,
    val phoneNumber: String,
    val pictureUrl: String,
    val lastMessage: String,
    val sentAt: LocalDateTime,
    val fromMe: Boolean,
    val aiAnswer: Boolean,
    val lastMessageAck: AckStatus,
){
    enum class AckStatus{
        PENDING, SENT, RECEIVED, READ
    }
}
