package com.blink.backend.persistence.repository.clinic;


import com.blink.backend.persistence.entity.clinic.ClinicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ClinicRepository extends JpaRepository<ClinicEntity, Integer> {

    Optional<ClinicEntity> findByWahaSession(String session);
    Optional<ClinicEntity> findByCode(String code);
}
