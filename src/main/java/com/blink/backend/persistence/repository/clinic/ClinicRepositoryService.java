package com.blink.backend.persistence.repository.clinic;

import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.persistence.entity.clinic.Clinic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicRepositoryService {
    private final ClinicRepository clinicRepository;

    public Clinic findById(Integer id) throws NotFoundException {
        return clinicRepository.findById(id).orElseThrow(() -> new NotFoundException("Clinica"));
    }

    public Optional<Clinic> findOptionalById(Integer id) {
        return clinicRepository.findById(id);
    }
}
