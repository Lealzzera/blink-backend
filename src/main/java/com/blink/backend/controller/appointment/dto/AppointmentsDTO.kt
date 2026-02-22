package com.blink.backend.controller.appointment.dto

import com.blink.backend.domain.model.Appointment
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AppointmentsDTO(
    val id: Int?,
    val patientName: String?,
    val patientPhone: String?,
    val time: LocalTime? ,
    val duration: Int?,
    val status: String?,
    val sales: MutableList<SaleDTO?>? = null,
    val notes: String?,
    /*
    * TODO private UserDTO attendedByUser; private LocalDateTime attendedAt
    * */
) {
    companion object {
        fun fromDomain(appointment: Appointment): AppointmentsDTO {
            return AppointmentsDTO(
                id = appointment.id,
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