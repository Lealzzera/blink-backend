package com.blink.backend.domain.integration.waha

import com.blink.backend.domain.integration.waha.dto.SendWahaMessageRequest
import com.blink.backend.domain.integration.waha.dto.SessionStatusWahaResponse
import com.blink.backend.domain.integration.waha.dto.WahaChatHistory
import com.blink.backend.domain.integration.waha.dto.WahaChatOverviewDto
import com.blink.backend.domain.integration.waha.dto.WahaConversationsDto
import com.blink.backend.domain.integration.waha.dto.WahaLid
import com.blink.backend.domain.integration.waha.dto.WahaPresenceDto

interface WahaClient {
    //auth endpoints
    fun getWahaQrCode(sessionName: String): ByteArray
    fun logoutSession(session: String)
    fun getWahaSessionStatus(session: String): SessionStatusWahaResponse
    fun createWahaSession(sessionName: String): SessionStatusWahaResponse
    fun deleteWahaSession(sessionName: String)

    //chat endpoints
    fun sendMessage(sendWahaMessageRequest: SendWahaMessageRequest)
    fun getMessages(session: String, chatId: String, limit: Int, offset: Int): List<WahaChatHistory>
    fun getOverview(session: String, limit: Int, offset: Int): List<WahaConversationsDto>
    fun sendSeen(wahaPresenceDto: WahaPresenceDto)
    fun startTyping(wahaPresenceDto: WahaPresenceDto)
    fun stopTyping(wahaPresenceDto: WahaPresenceDto)

    //contacts endpoints
    fun getPhoneNumberByLid(session: String, lid: String): WahaLid?
}