package com.blink.backend.controller.configuration.dto;


import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvailabilityConfigurationDTO {

    private Integer clinicId;
    private String weekDay;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime open;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime close;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakStart;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakEnd;
    @JsonProperty("is_work_day")
    private Boolean workDay;


    public static AvailabilityConfigurationDTO fromEntity(ClinicAvailability clinicAvailability){
        return AvailabilityConfigurationDTO.builder()
                .weekDay(clinicAvailability.getWeekDay().name())
                .open(clinicAvailability.getOpenTime())
                .close(clinicAvailability.getCloseTime())
                .breakStart(clinicAvailability.getLunchStartTime())
                .breakEnd(clinicAvailability.getLunchEndTime())
                .workDay(clinicAvailability.getIsWorkingDay())
                .build();
    }
}
