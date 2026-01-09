package com.blink.backend.domain.integration.waha

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "waha")
data class WahaProperties(
    val baseUrl: String,
    val webhookUrl: String,
    val apiKey: String,
    val receiveMessageWebhook: String,
    val sessionStatusWebhook: String,
)