package com.blink.blink_backend.persistence.entity.appointment;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) //Verificar se necessario
    @Column(name = "patient_id")
    private Patient patient;


    @Column(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "sale_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal saleValue;

    @ManyToOne(optional = false) //Verificar se necessario
    @Column(name = "sale_registered_by_user_id")
    private int registeredByUser;

    @Column(name = "sale_registered_at", nullable = false)
    private LocalDateTime saleRegisteredAt;

    @ManyToOne(optional = false)
    @Column(name = "service_type_id")
    private ServiceType serviceType;



}
