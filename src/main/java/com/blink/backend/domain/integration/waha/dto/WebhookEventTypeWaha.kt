package com.blink.backend.domain.integration.waha.dto

import com.fasterxml.jackson.annotation.JsonValue

enum class WebhookEventTypeWaha (private val value: String) {
    MESSAGE("message"),
    MESSAGE_ANY("message.any"),
    SESSION_STATUS("session.status"),
    ;

    @JsonValue
    fun toValue(): String {
        return value
    }
}