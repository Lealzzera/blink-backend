package com.blink.backend.domain.chat.model

import java.time.LocalDateTime

data class WhatsAppConversationHistory(
    val message: String,
    val fromMe: Boolean,
    val sentAt: LocalDateTime,
    val ackStatus: WhatsAppAckStatus,
    val hasMedia: Boolean
)