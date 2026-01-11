package com.blink.backend.domain.integration.waha.dto

data class SessionStatusWahaResponse(
    val status: WahaSessionStatus,
    val me: MeSessionStatus? = null
)
