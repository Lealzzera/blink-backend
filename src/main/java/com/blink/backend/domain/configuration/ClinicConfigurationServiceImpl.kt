package com.blink.backend.domain.configuration

import com.blink.backend.controller.configuration.dto.AppointmentConfigurationDto
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

            clinicAvailability.openTime = configuration.open
            clinicAvailability.closeTime = configuration.close
            clinicAvailability.lunchStartTime = configuration.breakStart
            clinicAvailability.lunchEndTime = configuration.breakEnd
            clinicAvailability.isWorkingDay = configuration.workDay

            clinicAvailabilityRepository.save(clinicAvailability)
        }
    }

    override fun updateAppointmentConfiguration(clinic: Clinic, appointmentConfiguration: AppointmentConfigurationDto) {
        val clinicEntity = clinicRepositoryService.findByCode(clinic.code)
        val clinicConfiguration = clinicConfigurationRepository.findByClinicId(clinicEntity.id)

        appointmentConfiguration.duration?.let {
            clinicConfiguration.appointmentDuration = it
        }

        appointmentConfiguration.overbooking?.let {
            clinicConfiguration.allowOverbooking = it
        }

        clinicConfigurationRepository.save(clinicConfiguration)
    }

    override fun getAppointmentConfiguration(clinic: Clinic): AppointmentConfigurationDto {
        val clinicEntity = clinicRepositoryService.findByCode(clinic.code)
        val clinicConfiguration = clinicConfigurationRepository.findByClinicId(clinicEntity.id)

        return AppointmentConfigurationDto(
            duration = clinicConfiguration.appointmentDuration,
            overbooking = clinicConfiguration.allowOverbooking
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
            clinicConfigurationRepository.save(configuration)
        }
    }
}
