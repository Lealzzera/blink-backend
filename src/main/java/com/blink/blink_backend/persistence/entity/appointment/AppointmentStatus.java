package com.blink.blink_backend.persistence.entity.appointment;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "appointment_status")
public class AppointmentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "status")
    private String status;

}
