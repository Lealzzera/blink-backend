package com.blink.backend.persistence.patient

import com.blink.backend.persistence.entity.appointment.PatientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PatientRepositoryKt : JpaRepository<PatientEntity, Int> {
    fun findByPhoneNumber(phoneNumber: String): PatientEntity?
    fun findByClinicCodeAndPhoneNumber(clinicCode: String, phoneNumber: String): PatientEntity?
    fun findByCode(code: UUID): PatientEntity?
}
