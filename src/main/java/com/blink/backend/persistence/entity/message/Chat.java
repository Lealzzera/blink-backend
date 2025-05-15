package com.blink.backend.persistence.entity.message;

import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.clinic.Clinic;
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
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "is_ai_answer")
    private boolean isAiAnswer = true;

}
