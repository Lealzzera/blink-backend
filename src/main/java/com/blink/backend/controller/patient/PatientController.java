package com.blink.backend.controller.patient;

import com.blink.backend.controller.patient.dto.CreatePatientRequest;
import com.blink.backend.controller.patient.dto.PatientDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/patient")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Void> createPatient(@RequestBody CreatePatientRequest patient) throws NotFoundException {
        return ResponseEntity
                .created(URI.create(patientService.createPatient(patient).toString()))
                .build();
    }

    @GetMapping("{clinicId}/phone-number/{phone}")
    public ResponseEntity<PatientDto> getPatientByPhone(
            @PathVariable Integer clinicId,
            @PathVariable String phone)
            throws NotFoundException {
        return ResponseEntity.ok(patientService.findByClinicIdAndPhoneNumber(clinicId, phone));
    }

    @GetMapping("{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Integer id) throws NotFoundException {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }
}
