package com.blink.backend.controller.configuration;


import com.blink.backend.controller.configuration.dto.AppointmentConfigurationDTO;
import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDTO;
import com.blink.backend.domain.service.ClinicConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> updateAvailabilityConfiguration(@RequestBody List<AvailabilityConfigurationDTO> updateAvailabilityConfiguration){

        clinicConfigurationService.updateAvailabilityConfiguration(updateAvailabilityConfiguration);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("appointments")
    public ResponseEntity<Void> updateAppointmentConfiguration (@RequestBody AppointmentConfigurationDTO appointmentConfiguration){

        clinicConfigurationService.updateAppointmentConfiguration(appointmentConfiguration);

        return ResponseEntity.noContent().build();
    }

}
