package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {

}
