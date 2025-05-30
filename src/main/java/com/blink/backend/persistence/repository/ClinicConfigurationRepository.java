package com.blink.backend.persistence.repository;


import com.blink.backend.persistence.entity.clinic.ClinicConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicConfigurationRepository extends JpaRepository <ClinicConfiguration, Integer>{

    ClinicConfiguration findByClinicId(Integer clinicId);

}
