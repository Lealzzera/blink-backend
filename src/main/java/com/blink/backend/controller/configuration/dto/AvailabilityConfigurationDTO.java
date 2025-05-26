package com.blink.backend.controller.configuration.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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


}
