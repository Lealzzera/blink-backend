package com.blink.backend.controller.patient;

import com.blink.backend.persistence.entity.appointment.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("patient")
public class PatientController {

    public ResponseEntity<Void> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok().build();
    }
}
