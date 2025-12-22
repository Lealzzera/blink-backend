package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicAvailabilityRepository extends JpaRepository<ClinicAvailability, Integer> {
    ClinicAvailability findByWeekDayAndIsWorkingDayTrueAndClinicId(WeekDay weekDay, Integer clinicId);

    ClinicAvailability findByWeekDayAndIsWorkingDayTrueAndClinicCode(WeekDay weekDay, String clinicCode);

    Optional<ClinicAvailability> findByClinicIdAndWeekDayAndIsWorkingDayTrue(Integer id, WeekDay weekDay);
    Optional<ClinicAvailability> findByClinicCodeAndWeekDayAndIsWorkingDayTrue(String code, WeekDay weekDay);

    ClinicAvailability findByClinicIdAndWeekDay(Integer id, WeekDay weekDay);

    List<ClinicAvailability> findByClinicId(Integer clinicId);
}
