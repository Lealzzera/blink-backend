package com.blink.backend.domain.integration.waha

import org.springframework.stereotype.Service

@Service
class WahaPhoneResolverService(
    private val wahaClient: WahaClient,
) {

    companion object {
        private const val LID_SUFFIX = "@lid"
        private val INDIVIDUAL_SUFFIXES = listOf("@c.us", "@lid", "@s.whatsapp.net")
    }

    fun isIndividualChat(chatId: String): Boolean {
        return INDIVIDUAL_SUFFIXES.any { chatId.endsWith(it) }
    }

    fun resolvePhoneNumber(session: String, chatId: String): String? {
        return if (chatId.endsWith(LID_SUFFIX)) {
            val lid = chatId.substringBefore("@")
            wahaClient.getPhoneNumberByLid(session, lid)?.pn
        } else {
            chatId
        }
            ?.substringBefore("@")
            ?.substringAfter(":")
    }
}
