package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.clinic.chat.model.WhatsAppConversation
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.Instant
import java.time.ZoneId

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class WahaConversationsDto(
    val picture: String,
    val name: String,
    val lastMessage: LastMessageDto,
) {
    data class LastMessageDto(
        val timestamp: Long,
        val message: String,
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
        return WahaHelper
            .extractSender(lastMessage.data.sender, lastMessage.data.senderAlt)
    }

    fun toDomain(aiAnswer: Boolean, patientName: String): WhatsAppConversation {
        val sentAt = Instant.ofEpochMilli(lastMessage.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        return WhatsAppConversation(
            whatsAppName = name,
            phoneNumber = getSender(),
            patientName = patientName,
            aiAnswer = aiAnswer,
            pictureUrl = picture,
            lastMessage = lastMessage.message,
            sentAt = sentAt,
            fromMe = lastMessage.fromMe,
            lastMessageAck = lastMessage.ackStatus.toDomain()
        )
    }
}
