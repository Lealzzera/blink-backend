package com.blink.backend.controller.appointment.dto;

import com.blink.backend.persistence.entity.appointment.AppointmentEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
public class AppointmentDTO {
    private Integer id;
    private String name;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private String phone;
    private Integer duration;
    private String status;
    private List<SaleDTO> sales;

    public static AppointmentDTO fromEntity(AppointmentEntity appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .name(appointment.getPatient().getName())
                .phone(appointment.getPatient().getPhoneNumber())
                .duration(appointment.getDuration())
                .time(appointment.getScheduledTime().toLocalTime())
                .status(appointment.getAppointmentStatus().name())
                .sales(appointment.getSales().stream().map(SaleDTO::fromEntity).collect(Collectors.toList()))
                .build();

    }



}
