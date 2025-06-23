package com.blink.backend.domain.service;

import com.blink.backend.controller.patient.dto.CreatePatientRequest;
import com.blink.backend.controller.patient.dto.PatientDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.persistence.repository.PatientRepository;
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final ClinicRepositoryService clinicRepository;

    public PatientDto findByPhoneNumber(String patientNumber) throws NotFoundException {
        return patientRepository.findByPhoneNumber(patientNumber)
                .map(PatientDto::fromEntity)
                .orElseThrow(() -> new NotFoundException("Paciente"));
    }

    public Integer createPatient(CreatePatientRequest patientRequest) throws NotFoundException {
        Clinic clinic = clinicRepository.findById(patientRequest.getClinicId());
        Patient patient = Patient.builder()
                .clinic(clinic)
                .phoneNumber(patientRequest.getPhoneNumber())
                .name(patientRequest.getName())
                .build();
        return patientRepository.save(patient).getId();
    }

    public PatientDto getPatientById(Integer patientId) throws NotFoundException {
        return patientRepository.findById(patientId)
                .map(PatientDto::fromEntity)
                .orElseThrow(() -> new NotFoundException("Paciente"));
    }
}
