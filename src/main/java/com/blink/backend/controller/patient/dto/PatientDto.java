package com.blink.backend.controller.patient.dto;

import com.blink.backend.persistence.entity.appointment.Patient;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatientDto {
    private String name;
    private String phoneNumber;
    private String clinicName;

    public static PatientDto fromEntity(Patient patient) {
        return PatientDto.builder()
                .name(patient.getName())
                .phoneNumber(patient.getPhoneNumber())
                .clinicName(patient.getClinic().getClinicName())
                .build();
    }
}
