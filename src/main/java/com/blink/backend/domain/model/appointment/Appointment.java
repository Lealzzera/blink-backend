package com.blink.backend.domain.model.appointment;

import com.blink.backend.persistence.entity.auth.Users;
import com.blink.backend.persistence.entity.clinic.Clinic;

import java.time.LocalDateTime;

public class Appointment {

    private Long id;

    private Patient patient;

    private LocalDateTime scheduledTime;

    private Clinic clinic;

    private Integer duration;

    private AppointmentStatus appointmentStatus;

    private ServiceType serviceType;

    private String notes;

    private Users attendedByUser;

    private LocalDateTime attendedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
