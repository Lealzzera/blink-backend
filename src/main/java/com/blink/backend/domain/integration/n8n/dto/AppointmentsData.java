package com.blink.backend.domain.integration.n8n.dto;

import com.blink.backend.persistence.entity.appointment.Appointment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AppointmentsData {

    LocalDateTime scheduledTime;
    String appointmentStatus;
    Integer appointmentDuration;

    public static AppointmentsData fromAppointment (Appointment appointment){

        return AppointmentsData.builder()
                .scheduledTime(appointment.getScheduledTime())
                .appointmentStatus(appointment.getAppointmentStatus().name())
                .appointmentDuration(appointment.getDuration())
                .build();

    }

}
