package com.blink.blink_backend.persistence.entity.message;

import com.blink.blink_backend.persistence.entity.appointment.Patient;
import com.blink.blink_backend.persistence.entity.clinic.Clinic;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "patient_id")
    private Patient patient;


    @Column(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "is_ai_answer", nullable = false)
    private boolean isAiAnswer = true;

}
