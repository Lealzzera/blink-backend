package com.blink.backend.domain.exception.message;

import com.blink.backend.domain.exception.ConflictException;

public class WhatsAppNotConnectedException extends ConflictException {

    public WhatsAppNotConnectedException() {
        super("whatsapp.not.connected", "O whats app não esta conectado");
    }
}
