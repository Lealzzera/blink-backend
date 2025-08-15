package com.blink.backend.persistence.repository.clinic;


import com.blink.backend.persistence.entity.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ClinicRepository extends JpaRepository<Clinic, Integer> {

    Optional<Clinic> findByWahaSession(String session);
}
