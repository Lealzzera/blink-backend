package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicAvailabilityExceptionRepository extends JpaRepository<ClinicAvailabilityException, Integer> {

    Optional<ClinicAvailabilityException> findByExceptionDayAndClinicId(LocalDate exceptionDay, Integer clinicId);

    List<ClinicAvailabilityException> findByClinicId(Integer clinicId);

}
