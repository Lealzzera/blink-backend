package com.blink.backend.domain.integration.waha.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SendMessageDto(
    val session: String,
    @JsonProperty("chatId")
    val phoneNumber: String,
    val text: String,
)