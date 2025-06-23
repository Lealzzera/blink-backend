package com.blink.backend.domain.integration.waha.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WahaWebhookEventTypes {
    MESSAGE("message");
    @JsonValue
    private final String value;
}
