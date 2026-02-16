package com.blink.backend.controller.configuration.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentConfigurationDTO {

    private Integer clinicId;
    private Integer duration;
    private Boolean overbooking;

}
