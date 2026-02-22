package com.blink.backend.controller.chat.dto

import com.blink.backend.domain.integration.waha.dto.MeSessionStatus
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatus
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class WahaSessionStatusUpdate(val session: String, val payload: SessionPayload, val me: MeSessionStatus? = null) {
    data class SessionPayload(val status: WahaSessionStatus)
}