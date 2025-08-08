package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentsRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByScheduledTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    Integer countByScheduledTimeBetweenAndAppointmentStatusNot(LocalDateTime scheduledTime, LocalDateTime scheduledEnd, AppointmentStatus appointmentStatus);
    List<Appointment> findAllByPatientIdAndScheduledTimeAfter(Integer patientId, LocalDateTime localDateTime);

}
