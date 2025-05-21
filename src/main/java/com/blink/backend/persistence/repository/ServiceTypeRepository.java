package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {

}
