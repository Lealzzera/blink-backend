package com.blink.blink_backend.persistence.entity.appointment;


import com.blink.blink_backend.persistence.entity.auth.Users;
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
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "week_day_id")
    private WeekDay weekDay;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "lunch_start_time")
    private LocalTime lunchStartTime;

    @Column(name = "lunch_end_time")
    private LocalTime lunchEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private Users updatedByUserId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_working_day")
    private Boolean isWorkingDay = true;
}
