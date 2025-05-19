package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {

    AppointmentStatus findByStatusIgnoreCase(String status);

}
