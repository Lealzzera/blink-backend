package com.blink.backend.controller.configuration;

import com.blink.backend.controller.configuration.dto.AppointmentConfigurationDTO;
import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDTO;
import com.blink.backend.domain.model.auth.AuthenticatedUser;
import com.blink.backend.domain.service.ClinicConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/configuration")
public class ClinicConfigurationController {

    public final ClinicConfigurationService clinicConfigurationService;

    @GetMapping("clinic-id")
    public ResponseEntity<Integer> getClinicId(@AuthenticationPrincipal AuthenticatedUser user) {
        log.info("getClinicId user={}", user.getUserId());
        return ResponseEntity.ok(user.getClinic().getId());
    }

    @PutMapping("availability")
    public void updateAvailabilityConfiguration(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody List<AvailabilityConfigurationDTO> updateAvailabilityConfiguration) {
        clinicConfigurationService.updateAvailabilityConfiguration(updateAvailabilityConfiguration);
    }

    @PutMapping("appointments")
    public void updateAppointmentsConfiguration(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody AppointmentConfigurationDTO appointmentConfiguration) {
        clinicConfigurationService.updateAppointmentConfiguration(appointmentConfiguration);
    }

    @GetMapping("availability")
    public ResponseEntity<List<AvailabilityConfigurationDTO>> getAvailabilityConfiguration(
            @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(clinicConfigurationService.getAvailabilityConfiguration(user.getClinic().getId()));
    }

    @GetMapping("appointments")
    public ResponseEntity<AppointmentConfigurationDTO> getAppointmentsConfiguration(
            @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(clinicConfigurationService.getAppointmentConfiguration(user.getClinic().getId()));
    }

}
