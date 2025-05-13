package com.blink.blink_backend.persistence.entity.appointment;


import com.blink.blink_backend.persistence.entity.clinic.Clinic;
import com.blink.blink_backend.persistence.entity.clinic.ClinicConfiguration;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@Table(name = "clinic_availability")
public class ClinicAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "week_day_id", nullable = false)
    private WeekDay weekDay;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Column(name = "lunch_start_time")
    private LocalTime lunchStartTime;

    @Column(name = "lunch_end_time")
    private LocalTime lunchEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id", nullable = false)
    private ClinicConfiguration updatedByUserId; //REVISAR

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // = LocalDateTime.now();

    /*
    @Column(name = "is_working_day", nullable = false)
    private boolean isWorkingDay = true;
    */
}
