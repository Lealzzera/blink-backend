package com.blink.backend.controller.appointment.dto;

import com.blink.backend.domain.model.AppointmentUpdate;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateAppointmentDTO {
    private String notes;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime scheduledTime;
    private AppointmentStatus status;

    public AppointmentUpdate toDomain() {
        return new AppointmentUpdate(
                notes,
                scheduledTime,
                status
        );
    }
}
