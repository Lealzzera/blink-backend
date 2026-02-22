package com.blink.backend.controller.configuration.dto

import com.blink.backend.persistence.entity.appointment.ClinicAvailability
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AvailabilityConfigurationDto(
    val weekDay: String?,
    val open: LocalTime?,
    val close: LocalTime?,
    val breakStart: LocalTime?,
    val breakEnd: LocalTime?,
    val workDay: Boolean
) {
    init {
        if (workDay) {
            require(open != null) { "open is required when isWorkingDay is true" }
            require(close != null) { "close is required when isWorkingDay is true" }
        }
        require((breakStart == null) == (breakEnd == null)) {
            "breakStart and breakEnd must both be provided or both be null"
        }
    }

    companion object {
        fun fromEntity(clinicAvailability: ClinicAvailability): AvailabilityConfigurationDto {
            return AvailabilityConfigurationDto(
                weekDay = clinicAvailability.weekDay.name,
                open = clinicAvailability.openTime,
                close = clinicAvailability.closeTime,
                breakStart = clinicAvailability.lunchStartTime,
                breakEnd = clinicAvailability.lunchEndTime,
                workDay = clinicAvailability.isWorkingDay
            )
        }
    }
}
