package com.blink.backend.persistence.repository

import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AtypicalWorkdayRepository : JpaRepository<ClinicAvailabilityException, Int> {

    fun findByExceptionDayAndClinicId(exceptionDay: LocalDate, clinicId: Int): ClinicAvailabilityException?
}