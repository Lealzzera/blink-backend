package com.blink.backend.domain.clinic.chat.service

import com.blink.backend.domain.clinic.chat.model.WhatsAppStatus
import com.blink.backend.domain.model.Clinic

interface WhatsAppAuthService {
    fun getQrCodeByClinic(clinic: Clinic): ByteArray
    fun disconnectByClinic(clinic: Clinic): WhatsAppStatus
    fun getStatusByClinic(clinic: Clinic): WhatsAppStatus
}