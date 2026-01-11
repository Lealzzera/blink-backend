package com.blink.backend.domain.chat.model

import java.time.LocalDateTime

data class WhatsAppConversation(
    val whatsAppName: String? = null,
    val patientName: String,
    val phoneNumber: String,
    val pictureUrl: String,
    val lastMessage: String,
    val sentAt: LocalDateTime,
    val fromMe: Boolean,
    val aiAnswer: Boolean,
    val lastMessageAck: WhatsAppAckStatus,
)
