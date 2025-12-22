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
            patient = Patient(phoneNumber = patientNumber, name = patientName),
            scheduledTime = scheduledTime,
            notes = notes,
            clinic = clinic,
        )
    }
}