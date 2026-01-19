package com.blink.backend.domain.integration.waha.dto

import com.blink.backend.domain.util.LowerCamelCaseDto

data class SessionStatusWahaResponse(
    val status: WahaSessionStatus,
    val me: MeSessionStatus? = null
) : LowerCamelCaseDto()
