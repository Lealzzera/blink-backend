package com.blink.backend.domain.integration.n8n

import com.blink.backend.domain.integration.n8n.dto.MessageReceived
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class N8nClientImpl(
    private val n8nRestClient: RestClient,
    private val n8nProperties: N8nProperties,
) : N8nClient {

    private val logger = LoggerFactory.getLogger(N8nClientImpl::class.java)

    override fun receiveMessage(message: MessageReceived) {
        logger.info("Sending message to n8n, sender=${message.sender}, clinicCode=${message.clinicCode}")
        n8nRestClient.post()
            .uri(n8nProperties.receiveMessageEndpoint)
            .body(message)
            .exchange { _, response ->
                logger.info("Send message to n8n completed, sender=${message.sender}, statusCode=${response.statusCode}")
            }
    }
}