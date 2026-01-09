package com.blink.backend.domain.integration.waha.dto

class WahaHelper {
    companion object {

        private val VALID_SENDER_DOMAINS = listOf("@s.whatsapp.net", "@c.us")

        fun extractSender(sender: String, senderAlt: String): String {
            val rawSender =
                if (VALID_SENDER_DOMAINS.any(sender::contains))
                    sender
                else
                    senderAlt

            return rawSender
                .trim()
                .substringBefore(":")
                .substringBefore("@")
        }
    }
}