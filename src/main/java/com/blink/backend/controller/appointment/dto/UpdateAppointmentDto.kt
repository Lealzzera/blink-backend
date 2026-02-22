package com.blink.backend.controller.appointment.dto

import com.blink.backend.domain.model.AppointmentUpdate
import com.blink.backend.persistence.entity.appointment.AppointmentStatus
import java.time.LocalDateTime

data class UpdateAppointmentDto(
    val notes: String? = null,
    val scheduledTime: LocalDateTime? = null,
    val status: AppointmentStatus? = null,
) {
    fun toDomain(): AppointmentUpdate {
        return AppointmentUpdate(
            notes,
            scheduledTime,
            status
        )
    }
}