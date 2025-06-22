package com.blink.backend.controller.patient.dto;

import lombok.Getter;

@Getter
public class CreatePatientRequest {
    private String name;
    private String phoneNumber;
    private Integer clinicId;
}
