package com.blink.backend.domain.service

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO
import com.blink.backend.domain.model.Appointment
import com.blink.backend.domain.model.Clinic
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
interface AppointmentsService {

    fun createAppointment(appointment: Appointment): String

    fun updateAppointment(id: Int, appointment: Appointment): Appointment?

    fun getScheduledAppointmentsOnDateRange(
        clinic: Clinic,
        startDate: LocalDate,
        endDate: LocalDate,
        hideCancelled: Boolean
    ): List<ClinicAvailabilityDTO>
}