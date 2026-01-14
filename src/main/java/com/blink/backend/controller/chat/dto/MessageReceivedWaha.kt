package com.blink.backend.controller.chat.dto

import com.blink.backend.domain.chat.model.WhatsAppMessage
import com.blink.backend.domain.integration.waha.dto.AckStatus
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

data class MessageReceivedWaha(
    val session: String,
    val payload: WahaPayload
) {

    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
    data class WahaPayload(
        val timestamp: Long,
        @JsonProperty("body") val message: String,
        val hasMedia: Boolean,
        val fromMe: Boolean,
        val ackName: AckStatus,
        val from: String,
    )

    fun toDomain(): WhatsAppMessage {
        return WhatsAppMessage(
            session = session,
            phoneNumber = payload.from,
            message = payload.message,
            fromMe = payload.fromMe,
            ackStatus = payload.ackName.toDomain(),
            timestamp = payload.timestamp,
            hasMedia = payload.hasMedia
        )
    }
}