package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByPhoneNumber(String patientNumber);

    Optional<Patient> findByClinic_IdAndPhoneNumber(Integer clinicId, String patientNumber);

}
