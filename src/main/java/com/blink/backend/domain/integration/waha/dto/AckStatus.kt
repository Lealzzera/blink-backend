package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.clinic.chat.model.WhatsAppConversation

enum class AckStatus {
    ERROR,
    PENDING,
    SERVER,
    DEVICE,
    READ,
    PLAYED,
    ;

    fun toDomain(): WhatsAppConversation.AckStatus {
        return when (this) {
            PENDING -> WhatsAppConversation.AckStatus.PENDING
            SERVER -> WhatsAppConversation.AckStatus.SENT
            DEVICE -> WhatsAppConversation.AckStatus.RECEIVED
            READ -> WhatsAppConversation.AckStatus.READ
            else -> WhatsAppConversation.AckStatus.OTHER
        }
    }
}