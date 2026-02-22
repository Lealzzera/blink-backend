package com.blink.backend.persistence.repository.clinic;

import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.persistence.entity.clinic.ClinicEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicRepositoryService {
    private final ClinicRepository clinicRepository;
    private static final String NOT_FOUND_MESSAGE = "Clinica";

    public ClinicEntity findById(Integer id) throws NotFoundException {
        return clinicRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    public ClinicEntity findByCode(String code) throws NotFoundException {
        return clinicRepository.findByCode(code).orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    public ClinicEntity findByWahaSession(String session) throws NotFoundException {
        return clinicRepository.findByWahaSession(session).orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    public ClinicEntity save(ClinicEntity clinic) {
        return clinicRepository.save(clinic);
    }
}
