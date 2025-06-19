package com.blink.backend.controller.message.dto;

import com.blink.backend.domain.model.message.WhatsAppStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import static com.blink.backend.domain.model.message.WhatsAppStatus.CONNECTED;
import static com.blink.backend.domain.model.message.WhatsAppStatus.SHUTDOWN;

@Getter
@Builder
public class WhatsAppStatusDto {
    private WhatsAppStatus status;
    private String connectedPhoneNumber;

    @JsonIgnore
    public boolean isShutdown() {
        return SHUTDOWN.equals(status);
    }

    @JsonIgnore
    public boolean isConnected() {
        return CONNECTED.equals(status);
    }

    @JsonIgnore
    public boolean isNotConnected() {
        return !isConnected();
    }
}
