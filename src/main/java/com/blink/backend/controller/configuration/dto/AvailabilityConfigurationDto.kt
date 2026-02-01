package com.blink.backend.controller.configuration.dto

import com.blink.backend.persistence.entity.appointment.ClinicAvailability
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AvailabilityConfigurationDto(
    val weekDay: String?,
    @JsonFormat(pattern = "HH:mm")
    val open: LocalTime?,
    @JsonFormat(pattern = "HH:mm")
    val close: LocalTime?,
    @JsonFormat(pattern = "HH:mm")
    val breakStart: LocalTime?,
    @JsonFormat(pattern = "HH:mm")
    val breakEnd: LocalTime?,
    @JsonProperty("is_work_day")
    val workDay: Boolean?
) {
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
