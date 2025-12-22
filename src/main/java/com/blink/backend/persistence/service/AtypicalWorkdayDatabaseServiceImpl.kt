package com.blink.backend.persistence.service

import com.blink.backend.domain.model.Clinic
import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException
import com.blink.backend.persistence.repository.AtypicalWorkdayRepository
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AtypicalWorkdayDatabaseServiceImpl(
    val atypicalWorkdayRepository: AtypicalWorkdayRepository,
    val clinicRepositoryService: ClinicRepositoryService
) :
    AtypicalWorkdayDatabaseService {

    override fun findByDayAndClinic(
        scheduledDay: LocalDate,
        clinic: Clinic
    ): ClinicAvailabilityException? {
        val clinicEntity = clinicRepositoryService.findByCode(clinic.code)
        return atypicalWorkdayRepository
            .findByExceptionDayAndClinicId(scheduledDay, clinicEntity.id);
    }

}