package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicAvailabilityRepository extends JpaRepository<ClinicAvailability, Integer> {
    ClinicAvailability findByWeekDayNameAndIsWorkingDayTrue(String weekDay);
}
