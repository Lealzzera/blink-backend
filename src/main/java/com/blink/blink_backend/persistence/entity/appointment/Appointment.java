package com.blink.blink_backend.persistence.entity.appointment;

import com.blink.blink_backend.persistence.entity.auth.Users;
import com.blink.blink_backend.persistence.entity.clinic.Clinic;
import com.blink.blink_backend.persistence.entity.clinic.ClinicConfiguration;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) //Verificar se necessário
    @Column(name = "patient_id")
    private Patient patient;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @ManyToOne(optional = false) //Verificar se necessário
    @Column(name = "clinic_id")
    private Clinic clinic;

    @Column(nullable = false)
    private Integer duration; // minutos

    @ManyToOne(optional = false) //Verificar se necessário
    @Column(name = "appointment_status_id")
    private AppointmentStatus appointmentStatus;

    @ManyToOne(optional = false) //Verificar se necessário
    @Column(name = "service_type_id")
    private ServiceType serviceType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @Column(name = "attended_by_user_id")
    private int attendedByUser;

    @Column(name = "attended_at")
    private LocalDateTime attendedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt; // = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // = LocalDateTime.now();

}
