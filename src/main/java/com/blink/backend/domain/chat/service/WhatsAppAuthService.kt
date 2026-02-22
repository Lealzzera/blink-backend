package com.blink.backend.domain.chat.service

import com.blink.backend.domain.chat.model.WhatsAppStatus
import com.blink.backend.domain.model.Clinic

interface WhatsAppAuthService {
    fun getQrCodeByClinic(clinic: Clinic): ByteArray
    fun disconnectByClinic(clinic: Clinic): WhatsAppStatus
    fun getStatusByClinic(clinic: Clinic): WhatsAppStatus
}