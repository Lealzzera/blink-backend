package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityExceptionDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException;
import com.blink.backend.persistence.entity.clinic.ClinicEntity;
import com.blink.backend.persistence.repository.ClinicAvailabilityExceptionRepository;
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicAvailabilityExceptionService {
    private final ClinicAvailabilityExceptionRepository clinicAvailabilityExceptionRepository;
    private final ClinicRepositoryService clinicRepository;

    public Integer createAvailabilityException(ClinicAvailabilityExceptionDTO availabilityExceptionDTO) throws NotFoundException {

        ClinicEntity clinic = clinicRepository.findById(availabilityExceptionDTO.getClinicId());
        ClinicAvailabilityException clinicAvailabilityException = clinicAvailabilityExceptionRepository
                .findByExceptionDayAndClinicId(availabilityExceptionDTO.getExceptionDay(), clinic.getId())
                .orElse(ClinicAvailabilityException
                        .builder()
                        .clinic(clinic)
                        .exceptionDay(availabilityExceptionDTO.getExceptionDay())
                        .isWorkingDay(availabilityExceptionDTO.getIsWorkingDay())
                        .openTime(availabilityExceptionDTO.getOpen())
                        .closeTime(availabilityExceptionDTO.getClose())
                        .lunchStartTime(availabilityExceptionDTO.getBreakStart())
                        .lunchEndTime(availabilityExceptionDTO.getBreakEnd())
                        .build());

        ClinicAvailabilityException exception = clinicAvailabilityExceptionRepository.save(clinicAvailabilityException);

        return exception.getId();
    }

    public ClinicAvailabilityExceptionDTO getClinicAvailabilityExceptionById(Integer id) throws NotFoundException {
        return clinicAvailabilityExceptionRepository.findById(id)
                .map(ClinicAvailabilityExceptionDTO::fromEntity)
                .orElseThrow(() -> new NotFoundException("Exceção"));
    }

    public List<ClinicAvailabilityExceptionDTO> getClinicAvailabilityExceptionByClinic(Integer clinicId) {
        return clinicAvailabilityExceptionRepository.findByClinicIdAndExceptionDayAfterOrderByExceptionDayDesc(clinicId,
                        LocalDate.now().minusDays(7))
                .stream().map(ClinicAvailabilityExceptionDTO::fromEntity)
                .toList();
    }

    public void deleteClinicAvailabilityExceptionById(Integer id) throws NotFoundException {
        try {
            clinicAvailabilityExceptionRepository.deleteById(id);
        } catch (OptimisticLockingFailureException ex) {
            throw new NotFoundException("Exceção");
        }
    }
}
