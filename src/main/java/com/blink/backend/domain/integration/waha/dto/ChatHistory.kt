package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.chat.model.WhatsAppConversationHistory
import com.blink.backend.domain.util.LowerCamelCaseDto
import java.time.Instant
import java.time.ZoneId

data class ChatHistory(
    val timestamp: Long,
    val fromMe: Boolean,
    val body: String?,
    val ackName: AckStatus,
    val hasMedia: Boolean,
    val media: Media? = null,
) : LowerCamelCaseDto() {
    fun toDomain(): WhatsAppConversationHistory {
        val message = when {
            hasMedia && media?.mimetype != null -> "[${media.mimetype}] ${body ?: ""}"
            hasMedia -> "[media] ${body ?: ""}"
            else -> body ?: ""
        }
        return WhatsAppConversationHistory(
            messageText = message.trim(),
            fromMe = fromMe,
            sentAt = Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(),
            ackStatus = ackName.toDomain(),
            hasMedia = hasMedia,
        )
    }

    data class Media(
        val mimetype: String? = null,
        val filename: String? = null,
        val url: String? = null,
    )
}