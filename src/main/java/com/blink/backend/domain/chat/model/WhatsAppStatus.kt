package com.blink.backend.domain.chat.model

data class WhatsAppStatus(
    val whatsAppNumber: String? = null,
    val status: Status,
) {

    enum class Status {
        CONNECTED,
        DISCONNECTED,
    }
}
