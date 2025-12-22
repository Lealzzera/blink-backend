package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {

    Optional<PatientEntity> findByPhoneNumber(String patientNumber);

    Optional<PatientEntity> findByClinic_IdAndPhoneNumber(Integer clinicId, String patientNumber);

}
