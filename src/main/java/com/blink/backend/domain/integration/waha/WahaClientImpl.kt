package com.blink.backend.domain.integration.waha

import com.blink.backend.domain.exception.NotFoundException
import com.blink.backend.domain.integration.waha.dto.CreateSessionWahaDto
import com.blink.backend.domain.integration.waha.dto.SendWahaMessageRequest
import com.blink.backend.domain.integration.waha.dto.SessionStatusWahaResponse
import com.blink.backend.domain.integration.waha.dto.WahaChatHistory
import com.blink.backend.domain.integration.waha.dto.WahaChatOverviewDto
import com.blink.backend.domain.integration.waha.dto.WahaPresenceDto
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatus
import com.blink.backend.domain.integration.waha.dto.WebhookEventTypeWaha
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import kotlin.collections.List

@Service
class WahaClientImpl(
    private val wahaRestClient: RestClient,
    private val wahaProperties: WahaProperties,
) : WahaClient {

    override fun getWahaQrCode(sessionName: String): ByteArray {
        return wahaRestClient.get()
            .uri("/api/{sessionName}/auth/qr", sessionName)
            .retrieve()
            .body(ByteArray::class.java)!!
    }

    override fun logoutSession(session: String) {
        wahaRestClient.post()
            .uri("/api/sessions/{session}/stop", session)
            .retrieve()
            .toBodilessEntity()
    }

    override fun getWahaSessionStatus(session: String): SessionStatusWahaResponse {
        return wahaRestClient.get()
            .uri("/api/sessions/{session}", session)
            .exchange { _, response ->
                when (response.statusCode) {
                    HttpStatus.NOT_FOUND -> SessionStatusWahaResponse(WahaSessionStatus.FAILED)
                    HttpStatus.OK -> response.bodyTo(SessionStatusWahaResponse::class.java)
                    else -> error("Session status request failed")
                }
            }
            ?: SessionStatusWahaResponse(WahaSessionStatus.FAILED)
    }

    override fun createWahaSession(sessionName: String): SessionStatusWahaResponse {
        val receiveMessageWebhook = CreateSessionWahaDto.WebhookEndpoint(
            url = "${wahaProperties.webhookUrl}${wahaProperties.receiveMessageWebhook}",//TODO review baseurl and paths
            event = WebhookEventTypeWaha.MESSAGE_ANY,
        )
        val sessionStatusWebhook = CreateSessionWahaDto.WebhookEndpoint(
            url = "${wahaProperties.webhookUrl}${wahaProperties.sessionStatusWebhook}",
            event = WebhookEventTypeWaha.SESSION_STATUS,
        )

        val requestBody = CreateSessionWahaDto.withWebhooks(
            name = sessionName,
            webhooks = listOf(receiveMessageWebhook, sessionStatusWebhook),
            authKey = wahaProperties.apiKey
        )
        return wahaRestClient.post()
            .uri("/api/sessions")
            .body(requestBody)
            .retrieve()
            .body(SessionStatusWahaResponse::class.java)
            ?: SessionStatusWahaResponse(WahaSessionStatus.FAILED)
    }

    override fun deleteWahaSession(sessionName: String) {
        wahaRestClient.delete()
            .uri("/api/sessions/{sessionName}", sessionName)
            .retrieve()
            .toBodilessEntity()
    }

    // ============================================== CHAT METHODS ==============================================
    override fun sendMessage(sendWahaMessageRequest: SendWahaMessageRequest) {
        wahaRestClient.post()
            .uri("/api/sendText")
            .body(sendWahaMessageRequest)
            .retrieve()
            .toBodilessEntity()
    }

    override fun getMessages(
        session: String,
        chatId: String,
        limit: Int,
        offset: Int
    ): List<WahaChatHistory> {
        return wahaRestClient.get()
            .uri(
                "/api/{session}/chats/{chatId}/messages?limit={limit}&offset={offset}",
                session, chatId, limit, offset
            )
            .retrieve()
            .body(object : ParameterizedTypeReference<List<WahaChatHistory>>() {})
            ?: emptyList()
    }

    /**
     *  @throws NotFoundException
     * */
    override fun getOverview(
        session: String,
        limit: Int,
        offset: Int
    ): List<WahaChatOverviewDto> {
        return wahaRestClient.get()
            .uri("/api/{session}/chats/overview?limit={limit}&offset={offset}", session, limit, offset)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals) { _, _ -> throw NotFoundException("Conversas") }
            .body(object : ParameterizedTypeReference<List<WahaChatOverviewDto>>() {})
            ?: emptyList()
    }

    override fun sendSeen(wahaPresenceDto: WahaPresenceDto) {
        wahaRestClient.post()
            .uri("/api/sendSeen")
            .body(wahaPresenceDto)
            .retrieve()
            .toBodilessEntity()
    }


    override fun startTyping(wahaPresenceDto: WahaPresenceDto) {
        wahaRestClient.post()
            .uri("/api/startTyping")
            .body(wahaPresenceDto)
            .retrieve()
            .toBodilessEntity()
    }

    override fun stopTyping(wahaPresenceDto: WahaPresenceDto) {
        wahaRestClient.post()
            .uri("/api/stopTyping")
            .body(wahaPresenceDto)
            .retrieve()
            .toBodilessEntity()
    }

}