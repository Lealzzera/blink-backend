package com.blink.backend.controller.appointment.dto;

import com.blink.backend.persistence.entity.appointment.Appointment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppointmentDetailsDTO {
    private Integer id;
    private PatientDTO patient;
    private LocalDateTime scheduledTime;
    private Integer clinicId;
    private Integer duration;
    private String appointmentStatus;
    private String serviceType;
    private String notes;
    private UserDTO attendedByUser;
    private LocalDateTime attendedAt;
    private List<SaleDTO> sales;

    public static AppointmentDetailsDTO fromEntity(Appointment appointment) {
        PatientDTO patientDTO = PatientDTO.builder()
                .name(appointment.getPatient().getName())
                .phoneNumber(appointment.getPatient().getPhoneNumber())
                .build();
        UserDTO userDTO = null;
        if (!Objects.isNull(appointment.getAttendedByUser())) {
            userDTO = UserDTO.builder()
                    .id(appointment.getAttendedByUser().getId())
                    .name(appointment.getAttendedByUser().getName())
                    .build();
        }
        return AppointmentDetailsDTO.builder()
                .id(appointment.getId())
                .patient(patientDTO)
                .scheduledTime(appointment.getScheduledTime())
                .duration(appointment.getDuration())
                .appointmentStatus(appointment.getAppointmentStatus().name())
                .serviceType(appointment.getServiceType().getServiceType())
                .notes(appointment.getNotes())
                .attendedByUser(userDTO)
                .clinicId(appointment.getClinic().getId())
                .sales(appointment.getSales().stream().map(SaleDTO::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
