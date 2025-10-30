package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardRepository extends CrudRepository<Appointment, Integer> {

    @Query("SELECT COUNT(p) " +
            "FROM Patient p " +
            "WHERE p.clinic.id = :clinicId " +
            "AND p.createdAt BETWEEN :startDate AND :endDate")
    Long countNovasMensagens(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(a) " +
            "FROM Appointment a " +
            "WHERE a.clinic.id = :clinicId " +
            "AND a.createdAt BETWEEN :startDate AND :endDate " +
            "AND a.appointmentStatus IN :appointmentStatus")
    Long countAppointmentsForDashboard(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("appointmentStatus") List<AppointmentStatus> appointmentStatus);

    @Query("SELECT COUNT(s) " +
            "FROM Sale s " +
            "WHERE s.patient.clinic.id = :clinicId " +
            "AND s.patient.createdAt BETWEEN :startDate AND :endDate")
    Long countVendas(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(s.value), 0) " +
            "FROM Sale s " +
            "WHERE s.patient.clinic.id = :clinicId " +
            "AND s.patient.createdAt BETWEEN :startDate AND :endDate")
    java.math.BigDecimal sumValorVendas(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
