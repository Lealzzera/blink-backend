package com.blink.backend.persistence.service

import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException
import java.time.LocalDate

interface AtypicalWorkdayDatabaseService {

    fun findByDayAndClinic(scheduledDay: LocalDate, clinicCode: String): ClinicAvailabilityException?
}