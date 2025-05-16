package com.blink.backend.controller.appointment.dto;

import com.blink.backend.persistence.entity.appointment.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class AppointmentDTO {
    private String name;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private String phone;
    private Integer duration;
    private String status;

    public static AppointmentDTO fromEntity(Appointment appointment) {
        return AppointmentDTO.builder()
                .name(appointment.getPatient().getName())
                .phone(appointment.getPatient().getPhoneNumber())
                .duration(appointment.getDuration())
                .time(appointment.getScheduledTime().toLocalTime())
                .status(appointment.getAppointmentStatus().getStatus())
                .build();

    }
}
