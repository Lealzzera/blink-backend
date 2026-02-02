package com.blink.backend.domain.integration.waha

import com.blink.backend.domain.integration.waha.dto.*

interface WahaClient {
    //auth endpoints
    fun getWahaQrCode(sessionName: String): ByteArray
    fun logoutSession(session: String)
    fun getWahaSessionStatus(session: String): SessionStatusWahaResponse
    fun createWahaSession(sessionName: String): SessionStatusWahaResponse
    fun deleteWahaSession(sessionName: String)

    //chat endpoints
    fun sendMessage(wahaMessageRequest: SendMessageDto)
    fun getMessages(session: String, chatId: String, limit: Int, offset: Int): List<ChatHistory>
    fun getOverview(session: String, limit: Int, offset: Int): List<WahaConversationsDto>
    fun sendSeen(wahaSessionChatDto: WahaSessionChatDto)
    fun startTyping(wahaSessionChatDto: WahaSessionChatDto)
    fun stopTyping(wahaSessionChatDto: WahaSessionChatDto)

    //contacts endpoints
    fun getPhoneNumberByLid(session: String, lid: String): WahaLid?
    fun getContact(session: String, contactId: String): WahaContactDto?
}