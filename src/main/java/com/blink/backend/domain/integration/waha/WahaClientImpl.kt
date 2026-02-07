package com.blink.backend.domain.integration.waha

import com.blink.backend.domain.exception.NotFoundException
import com.blink.backend.domain.integration.waha.dto.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class WahaClientImpl(
    private val wahaRestClient: RestClient,
    private val wahaProperties: WahaProperties,
    val objectMapper: ObjectMapper,
) : WahaClient {

    private val logger = LoggerFactory.getLogger(WahaClientImpl::class.java)

    override fun getWahaQrCode(sessionName: String): ByteArray {
        logger.info("Getting QR code, sessionName=$sessionName")
        return wahaRestClient.get()
            .uri("/api/{sessionName}/auth/qr", sessionName)
            .exchange { _, response ->
                logger.info("Get QR code completed, sessionName=$sessionName, statusCode=${response.statusCode}")
                response.bodyTo(ByteArray::class.java)
            }!!
    }

    override fun logoutSession(session: String) {
        logger.info("Logging out session, session=$session")
        wahaRestClient.post()
            .uri("/api/sessions/{session}/stop", session)
            .exchange { _, response ->
                logger.info("Logout session completed, session=$session, statusCode=${response.statusCode}")
            }
    }

    override fun getWahaSessionStatus(session: String): SessionStatusWahaResponse {
        logger.info("Getting session status, session=$session")
        return wahaRestClient.get()
            .uri("/api/sessions/{session}", session)
            .exchange { _, response ->
                logger.info("Get session status completed, session=$session, statusCode=${response.statusCode}")
                when (response.statusCode) {
                    HttpStatus.NOT_FOUND -> SessionStatusWahaResponse(WahaSessionStatus.FAILED)
                    HttpStatus.OK -> response.bodyTo(SessionStatusWahaResponse::class.java)
                    else -> error("Session status request failed")
                }
            }
            ?: SessionStatusWahaResponse(WahaSessionStatus.FAILED)
    }

    override fun createWahaSession(sessionName: String): SessionStatusWahaResponse {
        logger.info("Creating session, sessionName=$sessionName")
        val receiveMessageWebhook = CreateSessionWahaDto.WebhookEndpoint(
            url = "${wahaProperties.webhookUrl}${wahaProperties.receiveMessageWebhook}",
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
            .exchange { _, response ->
                logger.info("Create session completed, sessionName=$sessionName, statusCode=${response.statusCode}")
                response.bodyTo(SessionStatusWahaResponse::class.java)
            }
            ?: SessionStatusWahaResponse(WahaSessionStatus.FAILED)
    }

    override fun deleteWahaSession(sessionName: String) {
        logger.info("Deleting session, sessionName=$sessionName")
        wahaRestClient.delete()
            .uri("/api/sessions/{sessionName}", sessionName)
            .exchange { _, response ->
                logger.info("Delete session completed, sessionName=$sessionName, statusCode=${response.statusCode}")
            }
    }

    // ============================================== CHAT METHODS ==============================================
    override fun sendMessage(wahaMessageRequest: SendMessageDto) {
        logger.info(
            "Sending message, " +
                    "session=${wahaMessageRequest.session}, " +
                    "to=${wahaMessageRequest.phoneNumber}"
        )
        wahaRestClient.post()
            .uri("/api/sendText")
            .body(wahaMessageRequest)
            .exchange { _, response ->
                logger.info(
                    "Send message completed, " +
                            "session=${wahaMessageRequest.session}, " +
                            "to=${wahaMessageRequest.phoneNumber}, " +
                            "statusCode=${response.statusCode}"
                )
            }
    }

    override fun getMessages(
        session: String,
        chatId: String,
        limit: Int,
        offset: Int
    ): List<ChatHistory> {
        logger.info("Getting messages, session=$session, chatId=$chatId, limit=$limit, offset=$offset")
        return wahaRestClient.get()
            .uri(
                "/api/{session}/chats/{chatId}/messages?limit={limit}&offset={offset}",
                session, chatId, limit, offset
            )
            .exchange { _, response ->
                response.bodyTo(object : ParameterizedTypeReference<List<ChatHistory>>() {})
            }
            ?: emptyList()
    }

    /**
     *  @throws NotFoundException
     * */
    override fun getOverview(
        session: String,
        limit: Int,
        offset: Int
    ): List<WahaConversationsDto> {
        logger.info("Getting overview, session=$session, limit=$limit, offset=$offset")
        val nodes = wahaRestClient.get()
            .uri("/api/{session}/chats/overview?limit={limit}&offset={offset}", session, limit, offset)
            .exchange { _, response ->
                logger.info("Get overview completed, session=$session, statusCode=${response.statusCode}")
                if (response.statusCode == HttpStatus.NOT_FOUND) {
                    throw NotFoundException("Conversas")
                }
                response.bodyTo(object : ParameterizedTypeReference<List<JsonNode>>() {})
            }
            ?: emptyList()

        return nodes.mapNotNull {
            runCatching {
                objectMapper.treeToValue(it, WahaConversationsDto::class.java)
            }.onFailure { e ->
                logger.warn("Failed to deserialize conversation node: {}, error: {}", it, e.message)
            }.getOrNull()
        }
    }

    override fun sendSeen(wahaSessionChatDto: WahaSessionChatDto) {
        logger.info("Sending seen, session=${wahaSessionChatDto.session}, chatId=${wahaSessionChatDto.chatId}")
        wahaRestClient.post()
            .uri("/api/sendSeen")
            .body(wahaSessionChatDto)
            .exchange { _, response ->
                logger.info("Send seen completed, session=${wahaSessionChatDto.session}, chatId=${wahaSessionChatDto.chatId}, statusCode=${response.statusCode}")
            }
    }

    override fun startTyping(wahaSessionChatDto: WahaSessionChatDto) {
        logger.info("Starting typing, session=${wahaSessionChatDto.session}, chatId=${wahaSessionChatDto.chatId}")
        wahaRestClient.post()
            .uri("/api/startTyping")
            .body(wahaSessionChatDto)
            .exchange { _, response ->
                logger.info("Start typing completed, session=${wahaSessionChatDto.session}, chatId=${wahaSessionChatDto.chatId}, statusCode=${response.statusCode}")
            }
    }

    override fun stopTyping(wahaSessionChatDto: WahaSessionChatDto) {
        logger.info("Stopping typing, session=${wahaSessionChatDto.session}, chatId=${wahaSessionChatDto.chatId}")
        wahaRestClient.post()
            .uri("/api/stopTyping")
            .body(wahaSessionChatDto)
            .exchange { _, response ->
                logger.info("Stop typing completed, session=${wahaSessionChatDto.session}, chatId=${wahaSessionChatDto.chatId}, statusCode=${response.statusCode}")
            }
    }

    // ============================================== CONTACTS METHODS ==============================================
    override fun getPhoneNumberByLid(session: String, lid: String): WahaLid? {
        logger.info("Getting phone number by LID, session=$session, lid=$lid")
        return wahaRestClient.get()
            .uri("/api/{session}/lids/{lid}", session, lid)
            .exchange { _, response ->
                logger.info("Get phone number by LID completed, session=$session, lid=$lid, statusCode=${response.statusCode}")
                when (response.statusCode) {
                    HttpStatus.NOT_FOUND -> null
                    HttpStatus.OK -> response.bodyTo(WahaLid::class.java)
                    else -> null
                }
            }
    }

    override fun getContact(session: String, contactId: String): WahaContactDto? {
        logger.info("Getting contact, session=$session, contactId=$contactId")
        return wahaRestClient.get()
            .uri("/api/contacts?contactId={contactId}&session={session}", contactId, session)
            .exchange { _, response ->
                logger.info("Get contact completed, session=$session, contactId=$contactId, statusCode=${response.statusCode}")
                when (response.statusCode) {
                    HttpStatus.NOT_FOUND -> null
                    HttpStatus.OK -> response.bodyTo(WahaContactDto::class.java)
                    else -> null
                }
            }
    }

}