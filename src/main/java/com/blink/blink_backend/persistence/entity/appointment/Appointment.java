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
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "duration")
    private Integer duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_status_id")
    private AppointmentStatus appointmentStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "attended_by_user_id")
    private Users attendedByUser;

    @Column(name = "attended_at")
    private LocalDateTime attendedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
