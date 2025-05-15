package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicAvailabilityService {

    public List<ClinicAvailabilityDTO> getClinicAvailability(){
        ClinicAvailability clinicAvailability = new ClinicAvailability();
        List<Appointment> appointments = List.of();// TODO trazer do banco

        return List.of(ClinicAvailabilityDTO.fromEntity(clinicAvailability, appointments));
    }
}
