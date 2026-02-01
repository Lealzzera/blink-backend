package com.blink.backend.domain.configuration

import com.blink.backend.controller.configuration.dto.AppointmentConfigurationDto
import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDto
import com.blink.backend.controller.configuration.dto.ClinicConfigurationDto
import com.blink.backend.domain.model.Clinic

interface ClinicConfigurationService {

    fun getAvailabilityConfiguration(clinic: Clinic): List<AvailabilityConfigurationDto>

    fun updateAvailabilityConfiguration(clinic: Clinic, updateAvailabilityConfiguration: List<AvailabilityConfigurationDto>)

    fun updateAppointmentConfiguration(clinic: Clinic, appointmentConfiguration: AppointmentConfigurationDto)

    fun getAppointmentConfiguration(clinic: Clinic): AppointmentConfigurationDto

    fun updateClinicConfiguration(clinic: Clinic, clinicConfiguration: ClinicConfigurationDto)
}
