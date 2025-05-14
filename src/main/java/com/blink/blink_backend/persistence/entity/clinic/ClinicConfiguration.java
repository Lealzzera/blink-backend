package com.blink.blink_backend.persistence.entity.clinic;


import com.blink.blink_backend.persistence.entity.auth.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@Table(name = "clinic_configuration")
@Entity
public class ClinicConfiguration {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "whats_number")
    private String whatsNumber;

    @Column(name = "appointment_duration")
    private String appointmentDuration;

    @Column(name = "default_appointment_duration")
    private Integer defaultAppointmentDuration;

    @Column(name = "allow_overbooking")
    private Boolean allowOverbooking;

    @OneToOne
    @JoinColumn(name = "updated_by_user_id")
    private Users updatedByUserId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
