package com.blink.backend.persistence.entity.clinic;


import com.blink.backend.persistence.entity.auth.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Table(name = "clinic_configuration")
@Entity
public class ClinicConfiguration {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "whats_number")
    private String whatsNumber;

    @Column(name = "appointment_duration")
    private Integer appointmentDuration;

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
