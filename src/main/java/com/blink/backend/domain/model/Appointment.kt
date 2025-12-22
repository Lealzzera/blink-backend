package com.blink.backend.domain.model

import com.blink.backend.persistence.entity.appointment.AppointmentStatus
import java.time.LocalDateTime

data class Appointment(
    var code: String? = null,
    val patient: Patient,
    val clinic: Clinic,
    val scheduledTime: LocalDateTime? = null,
    val notes: String? = null,
    val status: AppointmentStatus? = null,
    val duration: Int? = null,
    val scheduledTimeEnd: LocalDateTime? = null,
) {

    fun updateAppointmentDuration(duration: Int): Appointment {
        return this.copy(
            duration = duration,
            scheduledTimeEnd = scheduledTime?.plusMinutes(duration.toLong())
        )
    }
}
