package com.blink.backend.domain.model

import java.util.UUID

data class Patient(
    val code: UUID?,
    val phoneNumber: String,
    val name: String,
    val aiAnswer: Boolean?
)