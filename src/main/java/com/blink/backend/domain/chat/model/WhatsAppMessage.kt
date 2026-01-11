package com.blink.backend.domain.chat.model

data class WhatsAppMessage (
    val session: String,
    val sender: String,
    val message: String,
    val fromMe: Boolean,
    val ackStatus: WhatsAppAckStatus,
    val timestamp: Long,
    val hasMedia: Boolean
)