package com.blink.backend.persistence.entity.clinic;


import com.blink.backend.persistence.entity.auth.UserEntity;
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
    private ClinicEntity clinic;

    @Column(name = "appointment_duration")
    private Integer appointmentDuration;

    @Column(name = "allow_overbooking")
    private Boolean allowOverbooking;

    @Column(name = "default_ai_answer")
    private Boolean defaultAiAnswer;

    @OneToOne
    @JoinColumn(name = "updated_by_user_id")
    private UserEntity updatedByUserId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public int getMaximumOverbookingAppointments() {
        return getAllowOverbooking() ? 2 : 1;
    }

}
