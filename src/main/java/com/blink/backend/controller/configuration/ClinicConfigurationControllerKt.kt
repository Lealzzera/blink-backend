package com.blink.backend.controller.configuration

import com.blink.backend.controller.configuration.dto.AppointmentConfigurationDto
import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDto
import com.blink.backend.controller.configuration.dto.ClinicConfigurationDto
import com.blink.backend.domain.configuration.ClinicConfigurationService
import com.blink.backend.domain.model.auth.AuthenticatedUser
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v2/configuration")
class ClinicConfigurationControllerKt(
    private val clinicConfigurationService: ClinicConfigurationService
) {

    @PutMapping("availability")
    fun updateAvailabilityConfiguration(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestBody updateAvailabilityConfiguration: List<AvailabilityConfigurationDto>
    ) {
        clinicConfigurationService.updateAvailabilityConfiguration(user.clinic.toDomain(), updateAvailabilityConfiguration)
    }

    @PutMapping("appointments")
    fun updateAppointmentsConfiguration(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestBody appointmentConfiguration: AppointmentConfigurationDto
    ) {
        clinicConfigurationService.updateAppointmentConfiguration(user.clinic.toDomain(), appointmentConfiguration)
    }

    @GetMapping("availability")
    fun getAvailabilityConfiguration(
@AuthenticationPrincipal user: AuthenticatedUser
    ): ResponseEntity<List<AvailabilityConfigurationDto>> {
        return ResponseEntity.ok(
            clinicConfigurationService.getAvailabilityConfiguration(user.clinic.toDomain())
        )
    }

    @GetMapping("appointments")
    fun getAppointmentsConfiguration(
        @AuthenticationPrincipal user: AuthenticatedUser
    ): ResponseEntity<AppointmentConfigurationDto> {
        return ResponseEntity.ok(
            clinicConfigurationService.getAppointmentConfiguration(user.clinic.toDomain())
        )
    }

    @PutMapping("clinic")
    fun updateClinicConfiguration(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestBody clinicConfiguration: ClinicConfigurationDto
    ): ResponseEntity<Unit> {
        clinicConfigurationService.updateClinicConfiguration(user.clinic.toDomain(), clinicConfiguration)
        return ResponseEntity.noContent().build()
    }
}