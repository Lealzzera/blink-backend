package com.blink.backend.controller.appointment.dto

import com.blink.backend.domain.model.WorkdayAvailability
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate
import java.time.LocalTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AvailabilityDTO(
    val isWorkday: Boolean,
    val date: LocalDate? = null,
    val open: LocalTime? = null,
    val close: LocalTime? = null,
    val breakStart: LocalTime? = null,
    val breakEnd: LocalTime? = null,
    val appointments: MutableList<AppointmentsDTO>? = null,
) {

    companion object {
        fun fromDomain(availability: WorkdayAvailability): AvailabilityDTO {
            return AvailabilityDTO(
                isWorkday = availability.isWorkDay,
                date = availability.date,
                open = availability.openTime,
                close = availability.closeTime,
                breakStart = availability.breakStartTime,
                breakEnd = availability.breakEndTime,
                appointments = availability.appointments
                    ?.stream()
                    ?.map { AppointmentsDTO.fromDomain(it) }
                    ?.toList()
            )
        }
    }
}
