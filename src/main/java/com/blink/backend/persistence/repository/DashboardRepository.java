package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import com.blink.backend.persistence.entity.clinic.Clinic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardRepository extends CrudRepository<Appointment, Integer> {

    @Query(value = "SELECT COUNT(*) " +
            "FROM blink_be_dev.patient " +
            "WHERE clinic_id = :clinicId " +
            "AND created_At BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Long countNovasMensagens(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT COUNT(*) " +
            "FROM blink_be_dev.appointment " +
            "WHERE clinic_id = :clinicId " +
            "AND created_at BETWEEN :startDate AND :endDate " +
            "AND status IN (:appointmentStatus)",
            nativeQuery = true)
    Long countAppointmentsForDashboard(
            Integer clinicId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<String> appointmentStatus);

    @Query(value = "SELECT COUNT(*) " +
            "FROM blink_be_dev.appointment " +
            "WHERE status IN ('AGENDADO', 'COMPARECEU') " +
            "AND clinic_id = :clinicId " +
            "AND created_at BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Long countAgendamentosRealizados(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT COUNT(*) " +
            "FROM blink_be_dev.appointment " +
            "WHERE status = 'COMPARECEU' " +
            "AND clinic_id = :clinicId " +
            "AND created_at BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Long countComparecimentos(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT COUNT(*) " +
            "FROM blink_be_dev.sale " +
            "WHERE patient_id IN (" +
            "SELECT id " +
            "FROM blink_be_dev.patient " +
            "WHERE clinic_id = :clinicId " +
            "AND registered_At BETWEEN :startDate AND :endDate)",
            nativeQuery = true)
    Long countVendas(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT COALESCE(SUM(value), 0) " +
            "FROM blink_be_dev.sale " +
            "WHERE patient_id IN (" +
            "SELECT id " +
            "FROM blink_be_dev.patient " +
            "WHERE clinic_id = :clinicId " +
            "AND registered_At BETWEEN :startDate AND :endDate)",
            nativeQuery = true)
    java.math.BigDecimal sumValorVendas(
            @Param("clinicId") Integer clinicId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
