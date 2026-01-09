package com.blink.backend.domain.service

import com.blink.backend.domain.model.Clinic
import com.blink.backend.domain.model.WorkdayAvailability
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
interface AvailabilityService {
    fun validateAppointmentTimeWithWorkdayShift(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        clinic: Clinic,
    )

    fun validateAppointmentTimeAvailability(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        clinic: Clinic,
        maximumAppointments: Int
    )

    fun getAvailabilityForDate(clinic: Clinic, date: LocalDate): WorkdayAvailability
}