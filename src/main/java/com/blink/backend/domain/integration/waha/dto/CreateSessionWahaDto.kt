package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.util.LowerCamelCaseDto
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CreateSessionWahaDto(
    val name: String, val start: Boolean = true, val config: SessionConfig
) : LowerCamelCaseDto() {
    data class SessionConfig(
        val webhooks: List<Webhook>, val noweb: NoWebConfig = NoWebConfig()
    ) : LowerCamelCaseDto() {
        data class Webhook(
            val url: String, val events: List<WebhookEventTypeWaha>, val customHeaders: List<CustomHeader>
        ) : LowerCamelCaseDto()

        data class CustomHeader(val name: String, val value: String) : LowerCamelCaseDto() {
            companion object {
                private const val API_KEY_HEADER = "X-Api-Key"

                fun apiKey(value: String) = CustomHeader(
                    name = API_KEY_HEADER, value = value
                )
            }
        }

        data class NoWebConfig(
            val store: StoreConfig = StoreConfig()
        ) : LowerCamelCaseDto()

        data class StoreConfig(
            val enabled: Boolean = true, val fullSync: Boolean = false
        ) : LowerCamelCaseDto()
    }


    companion object {
        fun withWebhooks(
            name: String,
            authKey: String,
            webhooks: List<WebhookEndpoint>,
        ): CreateSessionWahaDto = CreateSessionWahaDto(
            name = name, config = SessionConfig(
                webhooks = webhooks.map {
                    SessionConfig.Webhook(
                        url = it.url, events = listOf(it.event), customHeaders = listOf(
                            SessionConfig.CustomHeader.apiKey(authKey)
                        )
                    )
                })
        )
    }

    data class WebhookEndpoint(
        val event: WebhookEventTypeWaha, val url: String
    ) : LowerCamelCaseDto()
}

