package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.chat.model.WhatsAppConversation
import com.blink.backend.domain.util.LowerCamelCaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.Instant
import java.time.ZoneId

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class WahaConversationsDto(
    val picture: String,
    val name: String? = null,
    @JsonProperty("lastMessage") val lastMessage: LastMessageDto,
    @JsonProperty("_chat") val chat: WahaChatDto,
)  {
    data class WahaChatDto(
        val id: String,
    )
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
    data class LastMessageDto(
        val timestamp: Long,
        @JsonProperty("body") val message: String?,
        val hasMedia: Boolean,
        val fromMe: Boolean,
        val ackName: AckStatus,
        @JsonProperty("_data") val data: WahaConversationData,
    ) : LowerCamelCaseDto()

    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
    data class WahaConversationData(
        val info: WahaConversationInfo
    ) {
        @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
        data class WahaConversationInfo(
            val mediaType: String,
        )
    }

    fun getChatId(): String {
        return chat.id
    }

    fun toDomain(aiAnswer: Boolean, patientName: String, phoneNumber: String): WhatsAppConversation {
        val sentAt = Instant.ofEpochMilli(lastMessage.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        return WhatsAppConversation(
            whatsAppName = name,
            phoneNumber = phoneNumber,
            patientName = patientName,
            aiAnswer = aiAnswer,
            pictureUrl = picture,
            lastMessage = lastMessage.message ?: "Midia: ${lastMessage.data.info.mediaType}",
            sentAt = sentAt,
            fromMe = lastMessage.fromMe,
            lastMessageAck = lastMessage.ackName.toDomain()
        )
    }
}
