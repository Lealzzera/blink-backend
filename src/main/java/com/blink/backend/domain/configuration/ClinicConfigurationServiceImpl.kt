package com.blink.backend.domain.configuration

import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDto
import com.blink.backend.controller.configuration.dto.ClinicConfigurationDto
import com.blink.backend.domain.model.Clinic
import com.blink.backend.persistence.entity.appointment.WeekDay
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository
import com.blink.backend.persistence.repository.ClinicConfigurationRepository
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService
import org.springframework.stereotype.Service

@Service
class ClinicConfigurationServiceImpl(
    private val clinicAvailabilityRepository: ClinicAvailabilityRepository,
    private val clinicConfigurationRepository: ClinicConfigurationRepository,
    private val clinicRepositoryService: ClinicRepositoryService
) : ClinicConfigurationService {

    override fun getAvailabilityConfiguration(clinic: Clinic): List<AvailabilityConfigurationDto> {
        val clinicEntity = clinicRepositoryService.findByCode(clinic.code)
        return clinicAvailabilityRepository
            .findByClinicId(clinicEntity.id)
            .map { AvailabilityConfigurationDto.fromEntity(it) }
    }

    override fun updateAvailabilityConfiguration(clinic: Clinic, updateAvailabilityConfiguration: List<AvailabilityConfigurationDto>) {
        val clinicEntity = clinicRepositoryService.findByCode(clinic.code)
        for (configuration in updateAvailabilityConfiguration) {
            val clinicAvailability = clinicAvailabilityRepository
                .findByClinicIdAndWeekDay(
                    clinicEntity.id,
                    WeekDay.valueOf(configuration.weekDay!!)
                )

            configuration.open?.let { clinicAvailability.openTime = it }
            configuration.close?.let { clinicAvailability.closeTime = it }
            configuration.breakStart?.let { clinicAvailability.lunchStartTime = it }
            configuration.breakEnd?.let { clinicAvailability.lunchEndTime = it }
            configuration.workDay.let { clinicAvailability.isWorkingDay = it }

            clinicAvailabilityRepository.save(clinicAvailability)
        }
    }

    override fun getClinicConfiguration(clinic: Clinic): ClinicConfigurationDto {
        val clinicEntity = clinicRepositoryService.findByCode(clinic.code)
        val clinicConfiguration = clinicConfigurationRepository.findByClinicId(clinicEntity.id)

        return ClinicConfigurationDto(
            clinicName = clinicEntity.clinicName,
            aiName = clinicConfiguration.aiName,
            appointmentDuration = clinicConfiguration.appointmentDuration,
            allowOverbooking = clinicConfiguration.allowOverbooking,
            customPrompt = clinicConfiguration.customPrompt
        )
    }

    override fun updateClinicConfiguration(clinic: Clinic, clinicConfiguration: ClinicConfigurationDto) {
        val clinicEntity = clinicRepositoryService.findByCode(clinic.code)
        val configuration = clinicConfigurationRepository.findByClinicId(clinicEntity.id)

        clinicConfiguration.clinicName?.let {
            clinicEntity.clinicName = it
            clinicRepositoryService.save(clinicEntity)
        }

        clinicConfiguration.aiName?.let {
            configuration.aiName = it
        }

        clinicConfiguration.appointmentDuration?.let {
            configuration.appointmentDuration = it
        }

        clinicConfiguration.allowOverbooking?.let {
            configuration.allowOverbooking = it
        }

        clinicConfiguration.customPrompt?.let {
            configuration.customPrompt = it
        }

        clinicConfigurationRepository.save(configuration)
    }
}
