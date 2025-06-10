package com.blink.backend.controller.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClinicAvailabilityExceptionDTO {

    private Integer clinicId;
    private LocalDate exceptionDay;
    private Boolean isWorkingDay;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime lunchStartTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime lunchEndTime;

}
