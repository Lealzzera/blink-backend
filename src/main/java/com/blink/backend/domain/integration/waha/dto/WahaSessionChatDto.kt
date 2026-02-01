package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.util.LowerCamelCaseDto

data class WahaSessionChatDto(val chatId: String, val session: String) : LowerCamelCaseDto()
