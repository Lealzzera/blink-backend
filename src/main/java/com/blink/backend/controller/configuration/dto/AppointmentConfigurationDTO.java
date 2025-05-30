package com.blink.backend.controller.configuration.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppointmentConfigurationDTO {

    private Integer clinicId;
    private Integer duration;
    private Boolean overbooking;

}
