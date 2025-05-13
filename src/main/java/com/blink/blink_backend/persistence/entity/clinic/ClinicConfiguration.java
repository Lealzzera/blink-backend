package com.blink.blink_backend.persistence.entity.clinic;


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

    @Column(name = "whatsNumber")
    private String whatsNumber;

    @Column(name = "appointmentDuration")
    private String appointmentDuration;

    @Column(name = "defaultAppointmentDurantion")
    private int defaultAppointmentDurantion;

    @Column(name = "allowOverbooking")
    private boolean allowOverbooking;

    @Column(name = "updatedByUserId")
    private int updatedByUserId;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

}
