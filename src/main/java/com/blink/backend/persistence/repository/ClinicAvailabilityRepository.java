package com.blink.backend.persistence.repository;

import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDTO;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicAvailabilityRepository extends JpaRepository<ClinicAvailability, Integer> {
    ClinicAvailability findByWeekDayAndIsWorkingDayTrue(WeekDay weekDay);
    ClinicAvailability findByClinicIdAndWeekDayAndIsWorkingDayTrue(Integer id, WeekDay weekDay);

    ClinicAvailability findByClinicIdAndWeekDay(Integer id, WeekDay weekDay);

    List<ClinicAvailability> findByClinicId(Integer clinicId);
}
