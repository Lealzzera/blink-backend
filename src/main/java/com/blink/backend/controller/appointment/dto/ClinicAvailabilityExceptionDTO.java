package com.blink.backend.controller.appointment.dto;

import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException;
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
    private LocalTime open;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime close;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakStart;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakEnd;

    public static ClinicAvailabilityExceptionDTO fromEntity(ClinicAvailabilityException clinicAvailabilityException) {
        return ClinicAvailabilityExceptionDTO.builder()
                .clinicId(clinicAvailabilityException.getClinic().getId())
                .exceptionDay(clinicAvailabilityException.getExceptionDay())
                .isWorkingDay(clinicAvailabilityException.getIsWorkingDay())
                .open(clinicAvailabilityException.getOpenTime())
                .close(clinicAvailabilityException.getCloseTime())
                .breakStart(clinicAvailabilityException.getLunchStartTime())
                .breakEnd(clinicAvailabilityException.getLunchEndTime())
                .build();
    }
}
