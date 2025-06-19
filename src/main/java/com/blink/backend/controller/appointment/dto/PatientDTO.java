package com.blink.backend.controller.appointment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatientDTO {
    private String name;
    private String phoneNumber;
}
