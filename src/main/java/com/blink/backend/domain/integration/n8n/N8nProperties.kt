package com.blink.backend.domain.integration.n8n

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "n8n")
data class N8nProperties(
    val baseUrl: String,
    val receiveMessageEndpoint: String,
)