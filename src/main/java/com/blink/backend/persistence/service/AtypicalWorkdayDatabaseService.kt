package com.blink.backend.persistence.service

import com.blink.backend.domain.model.Clinic
import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
interface AtypicalWorkdayDatabaseService {

    fun findByDayAndClinic(scheduledDay: LocalDate, clinic: Clinic): ClinicAvailabilityException?
}