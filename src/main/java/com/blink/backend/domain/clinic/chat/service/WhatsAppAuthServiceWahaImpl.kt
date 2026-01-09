package com.blink.backend.domain.clinic.chat.service

import com.blink.backend.domain.clinic.chat.model.WhatsAppStatus
import com.blink.backend.domain.exception.message.WhatsAppAlreadyConnectedException
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException
import com.blink.backend.domain.integration.waha.WahaClient
import com.blink.backend.domain.integration.waha.dto.*
import com.blink.backend.domain.model.Clinic
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class WhatsAppAuthServiceWahaImpl(
    val wahaClient: WahaClient,
    private val logger: Logger = Logger.getLogger(WhatsAppAuthServiceWahaImpl::class.java.name)
) : WhatsAppAuthService {

    override fun getQrCodeByClinic(clinic: Clinic): ByteArray {
        val wahaStatus = getWahaSessionStatus(clinic)
        if (wahaStatus.isShutdown) {
            restartWahaSession(clinic)
        } else if (wahaStatus.isConnected) {
            throw WhatsAppAlreadyConnectedException()
        }
        logger.info { "getting-qr-code for clinic ${clinic.code}" }
        return wahaClient.getWahaQrCode(clinic.wahaSession)
    }

    override fun disconnectByClinic(clinic: Clinic): WhatsAppStatus {
        if (!getWahaSessionStatus(clinic).isConnected) {
            throw WhatsAppNotConnectedException()
        }
        wahaClient.logoutSession(clinic.wahaSession)
        return WhatsAppStatus(status = WhatsAppStatus.Status.DISCONNECTED)
    }

    override fun getStatusByClinic(clinic: Clinic): WhatsAppStatus {
        val response = wahaClient.getWahaSessionStatus(clinic.wahaSession)
        val status = if (response.status.isConnected)
            WhatsAppStatus.Status.CONNECTED
        else
            WhatsAppStatus.Status.DISCONNECTED

        return WhatsAppStatus(status = status, whatsAppNumber = response.me?.id)
    }

    private fun getWahaSessionStatus(clinic: Clinic): WahaSessionStatus {
        return wahaClient.getWahaSessionStatus(clinic.wahaSession).status
    }

    private fun restartWahaSession(clinic: Clinic) {
        logger.info { "Deleting old waha session" }
        wahaClient.deleteWahaSession(sessionName = clinic.wahaSession)
        wahaClient.createWahaSession(sessionName = clinic.wahaSession)
    }
}