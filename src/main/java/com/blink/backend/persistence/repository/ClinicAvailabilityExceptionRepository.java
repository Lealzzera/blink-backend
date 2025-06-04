package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicAvailabilityExceptionRepository extends JpaRepository<ClinicAvailabilityException, Integer> {

}
