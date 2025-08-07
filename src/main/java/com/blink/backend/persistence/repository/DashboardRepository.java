package com.blink.backend.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends CrudRepository <com.blink.backend.persistence.entity.clinic.Clinic, Integer> {

    @Query(value = "SELECT COUNT(*) FROM chat WHERE is_ai_answer = FALSE AND clinic_id = :clinicId", nativeQuery = true)
    Long countNovasMensagens(@Param("clinicId") Integer clinicId);

    @Query(value = "SELECT COUNT(*) FROM appointment WHERE status IN ('AGENDADO', 'COMPARECEU') AND clinic_id = :clinicId", nativeQuery = true)
    Long countAgendamentosRealizados(@Param("clinicId") Integer clinicId);

    @Query(value = "SELECT COUNT(*) FROM appointment WHERE status = 'COMPARECEU' AND clinic_id = :clinicId", nativeQuery = true)
    Long countComparecimentos(@Param("clinicId") Integer clinicId);

    @Query(value = "SELECT COUNT(*) FROM sale WHERE patient_id IN (SELECT id FROM patient WHERE clinic_id = :clinicId)", nativeQuery = true)
    Long countVendas(@Param("clinicId") Integer clinicId);

    @Query(value = "SELECT COALESCE(SUM(value), 0) FROM sale WHERE patient_id IN (SELECT id FROM patient WHERE clinic_id = :clinicId)", nativeQuery = true)
    java.math.BigDecimal sumValorVendas(@Param("clinicId") Integer clinicId);

}
