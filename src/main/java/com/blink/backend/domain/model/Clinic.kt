package com.blink.backend.domain.model

data class Clinic(
    val code: String,
    val name: String,
    val wahaSession: String? = null
)
