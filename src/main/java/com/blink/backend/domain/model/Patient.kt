package com.blink.backend.domain.model

data class Patient(
    val id: Int?,
    val phoneNumber: String,
    val name: String,
    val aiAnswer: Boolean?
)