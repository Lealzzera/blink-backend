package com.blink.backend.controller.configuration;

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityExceptionDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.service.ClinicAvailabilityExceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/configurations")
public class ExceptionConfigurationController {
    private final ClinicAvailabilityExceptionService clinicAvailabilityExceptionService;

    @PostMapping("availability/exception")//TODO irregular ou atypical ou alternative
    public ResponseEntity<Integer> createAvailabilityException(
            @RequestBody ClinicAvailabilityExceptionDTO availabilityExceptionDTO)
            throws NotFoundException {
        URI uri = URI.create(clinicAvailabilityExceptionService.createAvailabilityException(availabilityExceptionDTO).toString());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("availability/exception/{id}")
    public ResponseEntity<ClinicAvailabilityExceptionDTO> getClinicAvailabilityExceptionById(@PathVariable Integer id)
            throws NotFoundException {
        return ResponseEntity.ok(clinicAvailabilityExceptionService.getClinicAvailabilityExceptionById(id));
    }

    @GetMapping("availability/{clinicId}/exception")
    public ResponseEntity<List<ClinicAvailabilityExceptionDTO>> getClinicAvailabilityException(@PathVariable Integer clinicId) {

        return ResponseEntity.ok(clinicAvailabilityExceptionService.getClinicAvailabilityExceptionByClinic(clinicId));
    }

    @DeleteMapping("availability/exception/{id}")
    public ResponseEntity<Void> deleteAvailabilityException(@PathVariable Integer id) throws NotFoundException {
        clinicAvailabilityExceptionService.deleteClinicAvailabilityException(id);
        return ResponseEntity.noContent().build();
    }
}
