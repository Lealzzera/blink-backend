package com.blink.backend.domain.exception.appointment;

import com.blink.backend.domain.exception.ConflictException;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AppointmentConflictException extends ConflictException {
    public AppointmentConflictException(AppointmentConflitReason appointmentConflitReason) {
        super("appointment.conflict", appointmentConflitReason.message);
    }

    @Getter
    @AllArgsConstructor
    public enum AppointmentConflitReason {
        OVERLAP("Já existe outro agendamento neste horário"),
        OUTSIDE_WORK_HOURS("Tentativa fora do horário de funcionamento"),
        OUTSIDE_WORK_DAY("Tentativa fora do dia de funcionamento"),
        DURING_BREAK("Tentativa no horário de almoço ou pausa");

        private final String message;
    }
}
