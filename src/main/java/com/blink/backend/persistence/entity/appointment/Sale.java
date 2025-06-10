package com.blink.backend.persistence.entity.appointment;

import com.blink.backend.persistence.entity.auth.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "sale_value", precision = 10, scale = 2)
    private BigDecimal saleValue;

    @ManyToOne
    @JoinColumn(name = "sale_registered_by_user_id")
    private Users registeredByUser;

    @Column(name = "sale_registered_at")
    private LocalDateTime saleRegisteredAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;



}
