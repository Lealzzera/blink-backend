package com.blink.backend.controller.chat.dto

import com.blink.backend.domain.chat.model.WhatsAppMessage
import com.blink.backend.domain.integration.waha.dto.AckStatus
import com.blink.backend.domain.integration.waha.dto.WahaHelper
import com.fasterxml.jackson.annotation.JsonProperty
import jdk.jfr.internal.event.EventConfiguration.timestamp
import org.postgresql.translation.messages_bg

data class MessageReceivedWaha(
    val session: String,
    val payload: WahaPayload
) {

    data class WahaPayload(
        val timestamp: Long,
        @JsonProperty("body") val message: String,
        val hasMedia: Boolean,
        val fromMe: Boolean,
        val ackStatus: AckStatus,
        val data: WahaConversationData
    ) {

        data class WahaConversationData(
            val sender: String,
            val isGroup: Boolean,
            val senderAlt: String
        )
    }

    fun getSender(): String {
        return WahaHelper.extractSender(payload.data.sender, payload.data.senderAlt)
    }

    fun toDomain(): WhatsAppMessage {
        return WhatsAppMessage(
            session = session,
            sender = getSender(),
            message = payload.message,
            fromMe = payload.fromMe,
            ackStatus = payload.ackStatus.toDomain(),
            timestamp = payload.timestamp,
            hasMedia = payload.hasMedia,
        )
    }
}