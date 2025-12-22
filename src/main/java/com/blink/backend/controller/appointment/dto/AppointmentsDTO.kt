package com.blink.backend.controller.appointment.dto

import com.blink.backend.domain.model.Appointment
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AppointmentsDTO(
    val id: Int? = null,
    val patientName: String? = null,
    val patientPhone: String? = null,
    val time: LocalTime? = null,
    val duration: Int? = null,
    val status: String? = null,
    val sales: MutableList<SaleDTO?>? = null,
    val notes: String? = null,
) {
    companion object {
        fun fromDomain(appointment: Appointment): AppointmentsDTO {
            return AppointmentsDTO(
                id = 1,
                patientName = appointment.patient.name,
                patientPhone = appointment.patient.phoneNumber,
                time = appointment.scheduledTime?.toLocalTime(),
                duration = appointment.duration,
                status = appointment.status?.name,
                notes = appointment.notes,
            )
        }
    }

}