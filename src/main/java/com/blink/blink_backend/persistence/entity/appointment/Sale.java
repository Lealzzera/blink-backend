package com.blink.blink_backend.persistence.entity.appointment;

import com.blink.blink_backend.persistence.entity.auth.Users;
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
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

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
