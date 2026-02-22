package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.model.message.WhatsAppStatus

enum class WahaSessionStatus(val connectionStatus: WhatsAppStatus) {
    STOPPED(WhatsAppStatus.SHUTDOWN),
    STARTING(WhatsAppStatus.DISCONNECTED),
    SCAN_QR_CODE(WhatsAppStatus.DISCONNECTED),
    WORKING(WhatsAppStatus.CONNECTED),
    FAILED(WhatsAppStatus.SHUTDOWN),
    NOT_FOUND(WhatsAppStatus.SHUTDOWN),
    ;

    val isShutdown: Boolean
        get() = connectionStatus == WhatsAppStatus.SHUTDOWN

    val isConnected: Boolean
        get() = connectionStatus == WhatsAppStatus.CONNECTED
}