package com.blink.backend.controller.appointment.dto;

import com.blink.backend.persistence.entity.appointment.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppointmentDTO {
    private Integer id;
    private String name;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private String phone;
    private Integer duration;
    private String status;

    public static AppointmentDTO fromEntity(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .name(appointment.getPatient().getName())
                .phone(appointment.getPatient().getPhoneNumber())
                .duration(appointment.getDuration())
                .time(appointment.getScheduledTime().toLocalTime())
                .status(appointment.getAppointmentStatus().getStatus())
                .build();

    }
}
