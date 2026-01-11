package com.blink.backend.domain.chat.service

import com.blink.backend.domain.chat.model.WhatsAppMessage
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatus

interface WahaWebhookService {
    fun receiveMessage(whatsAppMessage: WhatsAppMessage)

    fun sessionStatusUpdated(session: String, status: WahaSessionStatus, phoneNumber: String? = null)
}