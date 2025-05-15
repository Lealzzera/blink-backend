package com.blink.backend.domain.model.clinic;


import com.blink.backend.persistence.entity.auth.Users;

import java.time.LocalDateTime;

public class ClinicConfiguration {

    private Long id;

    private Clinic clinic;

    private String whatsNumber;

    private String appointmentDuration;

    private Integer defaultAppointmentDuration;

    private Boolean allowOverbooking;

    private Users updatedByUserId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
