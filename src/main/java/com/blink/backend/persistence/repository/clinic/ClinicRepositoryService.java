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

    public ClinicEntity findById(Integer id) throws NotFoundException {
        return clinicRepository.findById(id).orElseThrow(() -> new NotFoundException("Clinica"));
    }

    public ClinicEntity findByCode(String code) throws NotFoundException {
        return clinicRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Clinica"));
    }

    public Optional<ClinicEntity> findOptionalById(Integer id) {
        return clinicRepository.findById(id);
    }

    public ClinicEntity findByWahaSession(String session) throws NotFoundException {
        return clinicRepository.findByWahaSession(session).orElseThrow(() -> new NotFoundException("Clinica"));
    }
}
