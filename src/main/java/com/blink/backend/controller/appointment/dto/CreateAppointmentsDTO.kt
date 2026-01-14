package com.blink.backend.controller.appointment.dto

import com.blink.backend.domain.model.Appointment
import com.blink.backend.domain.model.Clinic
import com.blink.backend.domain.model.Patient
import java.time.LocalDateTime

data class CreateAppointmentsDTO(
    val patientNumber: String,
    val patientName: String,
    val scheduledTime: LocalDateTime,
    val notes: String? = null,
) {
    fun toAppointment(clinic: Clinic): Appointment {
        return Appointment(
            id = null,
            patient = Patient(
                id = null,
                phoneNumber = patientNumber,
                name = patientName,
                aiAnswer = null
            ),
            scheduledTime = scheduledTime,
            notes = notes,
            clinic = clinic,
        )
    }
}