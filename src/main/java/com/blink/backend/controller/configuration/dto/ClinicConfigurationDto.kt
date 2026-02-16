package com.blink.backend.controller.configuration.dto

data class ClinicConfigurationDto(
    val clinicName: String?,
    val aiName: String?,
    val appointmentDuration: Int?,
    val allowOverbooking: Boolean?
)
