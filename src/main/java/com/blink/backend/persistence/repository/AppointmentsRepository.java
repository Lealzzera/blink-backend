package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentsRepository extends JpaRepository<Appointment, Integer> {

    /*@Query(value = "SELECT a.* " +
            "FROM Appointment a " +
            "WHERE DATE(a.scheduled_time) =:date",
            nativeQuery = true)*/
    List<Appointment> findByScheduledTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
