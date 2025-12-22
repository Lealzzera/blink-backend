package com.blink.backend.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class WorkdayAvailability(
    val isWorkDay: Boolean,
    val date: LocalDate,
    val openTime: LocalTime? = null,
    val closeTime: LocalTime? = null,
    val breakStartTime: LocalTime? = null,
    val breakEndTime: LocalTime? = null,
    val appointments: List<Appointment>? = null,
)