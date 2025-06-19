package com.blink.backend.domain.integration.waha.dto;

import com.blink.backend.domain.model.message.WhatsAppStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.blink.backend.domain.model.message.WhatsAppStatus.CONNECTED;
import static com.blink.backend.domain.model.message.WhatsAppStatus.DISCONNECTED;
import static com.blink.backend.domain.model.message.WhatsAppStatus.SHUTDOWN;

@Getter
@AllArgsConstructor
public enum WahaSessionStatus {
    STOPPED(SHUTDOWN),
    STARTING(DISCONNECTED),
    SCAN_QR_CODE(DISCONNECTED),
    WORKING(CONNECTED),
    FAILED(SHUTDOWN),
    NOT_FOUND(SHUTDOWN),
    ;

    private final WhatsAppStatus connectionStatus;

    public boolean isShutdown() {
        return connectionStatus == SHUTDOWN;
    }

    public boolean isConnected() {
        return connectionStatus == CONNECTED;
    }
}
