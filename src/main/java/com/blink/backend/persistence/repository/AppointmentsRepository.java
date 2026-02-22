package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.AppointmentEntity;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentsRepository extends JpaRepository<AppointmentEntity, Integer> {
    List<AppointmentEntity> findByScheduledTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    Integer countByScheduledTimeBetweenAndAppointmentStatusNot(LocalDateTime scheduledTime, LocalDateTime scheduledEnd, AppointmentStatus appointmentStatus);

    Integer countByClinicCodeAndScheduledTimeBetweenAndAppointmentStatusNot(String clinicCode, LocalDateTime scheduledTime, LocalDateTime scheduledEnd, AppointmentStatus appointmentStatus);

    List<AppointmentEntity> findAllByPatientCodeAndScheduledTimeAfter(java.util.UUID patientCode, LocalDateTime localDateTime);

}
