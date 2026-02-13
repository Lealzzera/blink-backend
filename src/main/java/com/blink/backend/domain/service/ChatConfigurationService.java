package com.blink.backend.domain.service;

import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.persistence.entity.appointment.PatientEntity;
import com.blink.backend.persistence.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatConfigurationService {
    private final PatientRepository patientRepository;

    public Boolean toggleAiAnswerMode(Integer clinicId, String phoneNumber) throws NotFoundException {
        PatientEntity patient = patientRepository.findByClinic_IdAndPhoneNumber(clinicId, phoneNumber)
                .orElseThrow(() -> new NotFoundException("Paciente"));
        patient.setAiAnswer(!patient.getAiAnswer());
        patientRepository.save(patient);
        return patient.getAiAnswer();
    }
}
