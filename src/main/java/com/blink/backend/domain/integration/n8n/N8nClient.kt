package com.blink.backend.domain.integration.n8n

import com.blink.backend.domain.integration.n8n.dto.MessageReceived

interface N8nClient {
    fun receiveMessage(message: MessageReceived)
}