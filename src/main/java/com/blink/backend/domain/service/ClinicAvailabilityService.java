package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicAvailabilityService {
    private final ClinicAvailabilityRepository clinicAvailabilityRepository;

    public List<ClinicAvailabilityDTO> getClinicAvailability(
            LocalDate startDate,
            LocalDate endDate) {
        List<ClinicAvailabilityDTO> days = startDate
                .datesUntil(endDate)
                .map(this::fromEntity)
                .toList();

        return days;
    }

    private ClinicAvailabilityDTO fromEntity(LocalDate dia){
        ClinicAvailability clinicAvailability = clinicAvailabilityRepository.findByWeekDayName(dia.getDayOfWeek().name());
        List<Appointment> appointments = List.of();//TODO trazer do banco
        return ClinicAvailabilityDTO.fromEntity(dia, clinicAvailability, appointments);
    }
}
