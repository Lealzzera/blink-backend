package com.blink.backend.controller.appointment.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAppointmentStatusDTO {

    private Integer appointmentId;
    private String newStatus;

}
