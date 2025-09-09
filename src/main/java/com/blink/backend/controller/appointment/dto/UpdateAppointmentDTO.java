package com.blink.backend.controller.appointment.dto;

import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateAppointmentDTO {
    private String notes;
    private LocalDateTime scheduledTime;
    private AppointmentStatus status;
}
