package com.blink.backend.controller.configuration;

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityExceptionDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.model.auth.AuthenticatedUser;
import com.blink.backend.domain.service.ClinicAvailabilityExceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/configuration/availability/atypical")
public class AtypicalAvailabilityConfigurationController {
    private final ClinicAvailabilityExceptionService clinicAvailabilityExceptionService;

    @PostMapping
    public ResponseEntity<Integer> createAvailabilityException(
            @RequestBody ClinicAvailabilityExceptionDTO availabilityExceptionDTO)
            throws NotFoundException {
        URI uri = URI.create(clinicAvailabilityExceptionService.createAvailabilityException(availabilityExceptionDTO).toString());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ClinicAvailabilityExceptionDTO>> getClinicAvailabilityException(
            @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(clinicAvailabilityExceptionService.getClinicAvailabilityExceptionByClinic(user.getClinic().getId()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ClinicAvailabilityExceptionDTO> getClinicAtypicalAvailabilityById(
            @PathVariable Integer id)
            throws NotFoundException {
        return ResponseEntity.ok(clinicAvailabilityExceptionService.getClinicAvailabilityExceptionById(id));
    }

    @PutMapping("{atypicalId}")
    public ResponseEntity<Void> updateClinicAvailabilityException(
            @PathVariable Integer atypicalId) {
        //TODO implement UPDATE
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{atypicalId}")
    public ResponseEntity<Void> deleteAvailabilityException(
            @PathVariable Integer atypicalId) throws NotFoundException {
        clinicAvailabilityExceptionService.deleteClinicAvailabilityExceptionById(atypicalId);
        return ResponseEntity.noContent().build();
    }
}
