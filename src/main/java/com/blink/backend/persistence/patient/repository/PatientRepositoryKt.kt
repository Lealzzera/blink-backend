package com.blink.backend.persistence.patient.repository

import com.blink.backend.persistence.entity.appointment.PatientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PatientRepositoryKt : JpaRepository<PatientEntity, Int> {
    fun findByPhoneNumber(phoneNumber: String): PatientEntity?
    fun findByClinicIdAndPhoneNumber(clinicId: Int, phoneNumber: String): PatientEntity?
    fun findByClinicCodeAndPhoneNumber(clinicCode: String, phoneNumber: String): PatientEntity?
}
