package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClinicAvailabilityService {
    private final ClinicAvailabilityRepository clinicAvailabilityRepository;
    private final AppointmentsRepository appointmentsRepository;

    public List<ClinicAvailabilityDTO> getClinicAvailability(
            LocalDate startDate,
            LocalDate endDate) {

        return startDate
                .datesUntil(endDate.plusDays(1))
                .map(this::fromEntity)
                .filter(Objects::nonNull)
                .toList();
    }

    private ClinicAvailabilityDTO fromEntity(LocalDate date) {
        ClinicAvailability clinicAvailability = clinicAvailabilityRepository
                .findByWeekDayNameAndIsWorkingDayTrue(date.getDayOfWeek().name());
        List<Appointment> appointments = appointmentsRepository
                .findByScheduledTimeBetween(
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay());

        return ClinicAvailabilityDTO.fromEntity(date, clinicAvailability, appointments);
    }
}
