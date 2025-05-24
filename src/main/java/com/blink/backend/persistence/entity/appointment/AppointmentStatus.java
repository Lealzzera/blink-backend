package com.blink.backend.persistence.entity.appointment;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
    AGENDADO, CANCELADO, COMPARECEU, NAO_COMPARECEU, CONFIRMADO
}
