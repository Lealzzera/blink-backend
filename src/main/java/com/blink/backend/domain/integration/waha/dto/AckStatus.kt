package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.chat.model.WhatsAppAckStatus

enum class AckStatus {
    ERROR,
    PENDING,
    SERVER,
    DEVICE,
    READ,
    PLAYED,
    UNKNOWN,
    ;

    fun toDomain(): WhatsAppAckStatus {
        return when (this) {
            PENDING ->  WhatsAppAckStatus.PENDING
            SERVER ->  WhatsAppAckStatus.SENT
            DEVICE ->  WhatsAppAckStatus.RECEIVED
            READ ->  WhatsAppAckStatus.READ
            else ->  WhatsAppAckStatus.OTHER
        }
    }
}