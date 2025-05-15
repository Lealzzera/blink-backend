package com.blink.backend.domain.model.appointment;

import com.blink.backend.persistence.entity.auth.Users;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale {

            private Long id;

            private Patient patient;

            private Appointment appointment;

        private BigDecimal saleValue;

            private Users registeredByUser;

        private LocalDateTime saleRegisteredAt;

            private ServiceType serviceType;



}
