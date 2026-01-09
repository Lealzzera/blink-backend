package com.blink.backend.domain.service

import com.blink.backend.domain.model.Appointment
import com.blink.backend.domain.model.AppointmentUpdate
import com.blink.backend.domain.model.Clinic
import com.blink.backend.domain.model.WorkdayAvailability
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
interface AppointmentsService {

    fun createAppointment(appointment: Appointment): String

    fun updateAppointment(
        clinic: Clinic,
        appointmentId: Int,
        appointment: AppointmentUpdate
    ): Appointment?

    fun getScheduledAppointmentsOnDateRange(
        clinic: Clinic,
        startDate: LocalDate,
        endDate: LocalDate,
        hideCancelled: Boolean
    ): List<WorkdayAvailability>
}