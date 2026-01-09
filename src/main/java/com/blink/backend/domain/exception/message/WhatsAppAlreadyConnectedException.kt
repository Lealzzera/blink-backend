package com.blink.backend.domain.exception.message

import com.blink.backend.domain.exception.ConflictException

class WhatsAppAlreadyConnectedException(
    code: String = "whatsapp.already.connected",
    message: String = "O WhatsApp já está conectado"
) : ConflictException(code, message)
