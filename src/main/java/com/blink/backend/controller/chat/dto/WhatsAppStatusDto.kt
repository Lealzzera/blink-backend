package com.blink.backend.controller.chat.dto

import com.blink.backend.domain.clinic.chat.model.WhatsAppStatus

data class WhatsAppStatusDto(val status: String, val connectedNumber: String?) {
    companion object {
        fun fromDomain(domain: WhatsAppStatus): WhatsAppStatusDto {
            return WhatsAppStatusDto(status = domain.status.name, connectedNumber = domain.whatsAppNumber)
        }
    }
}