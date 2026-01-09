package com.blink.backend.domain.model

import com.blink.backend.persistence.entity.appointment.AppointmentStatus
import java.time.LocalDateTime

data class AppointmentUpdate(
    val notes: String? = null,
    val scheduledTime: LocalDateTime? = null,
    val status: AppointmentStatus? = null,
)
