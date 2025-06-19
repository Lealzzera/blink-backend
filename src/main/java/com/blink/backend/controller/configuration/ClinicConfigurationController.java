package com.blink.backend.controller.configuration;


import com.blink.backend.controller.appointment.dto.ClinicAvailabilityExceptionDTO;
import com.blink.backend.controller.configuration.dto.AppointmentConfigurationDTO;
import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.service.ClinicConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("configurations")
public class ClinicConfigurationController {
    private final ClinicConfigurationService clinicConfigurationService;

    @PutMapping("availability")
    public ResponseEntity<Void> updateAvailabilityConfiguration(@RequestBody List<AvailabilityConfigurationDTO> updateAvailabilityConfiguration) {
        clinicConfigurationService.updateAvailabilityConfiguration(updateAvailabilityConfiguration);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("appointments")
    public ResponseEntity<Void> updateAppointmentConfiguration(@RequestBody AppointmentConfigurationDTO appointmentConfiguration) {
        clinicConfigurationService.updateAppointmentConfiguration(appointmentConfiguration);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("availability/{clinicId}")
    public ResponseEntity<List<AvailabilityConfigurationDTO>> getAvailabilityConfiguration(@PathVariable Integer clinicId) {
        return ResponseEntity.ok(clinicConfigurationService.getAvailabilityConfiguration(clinicId));
    }

    @GetMapping("appointments/{clinicId}")
    public ResponseEntity<AppointmentConfigurationDTO> getAppointmentConfiguration(@PathVariable Integer clinicId) {
        return ResponseEntity.ok(clinicConfigurationService.getAppointmentConfiguration(clinicId));
    }

    @PostMapping("availability/exception")
    public ResponseEntity<Void> createAvailabilityException(
            @RequestBody ClinicAvailabilityExceptionDTO availabilityExceptionDTO)
            throws NotFoundException {
        clinicConfigurationService.createAvailabilityException(availabilityExceptionDTO);
        return ResponseEntity.noContent().build();
    }

}
