package com.blink.backend.controller.appointment.dto;

import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ClinicAvailabilityDTO {
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime open;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime close;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakStart;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakEnd;
    private List<AppointmentDTO> appointments;

    public static ClinicAvailabilityDTO fromEntity(
            ClinicAvailability clinicAvailability,
            List<Appointment> appointments) {
        return ClinicAvailabilityDTO.builder()
                .date(LocalDate.now())
                .open(clinicAvailability.getOpenTime())
                .close(clinicAvailability.getCloseTime())
                .breakStart(clinicAvailability.getLunchStartTime())
                .breakEnd(clinicAvailability.getLunchEndTime())
                .appointments(appointments
                        .stream()
                        .map(AppointmentDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
