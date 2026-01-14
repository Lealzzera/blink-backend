package com.blink.backend.persistence.patient.repository

import com.blink.backend.persistence.entity.appointment.PatientEntity
import org.springframework.stereotype.Service

@Service
class PatientRepositoryService(
    private val patientRepository: PatientRepositoryKt,
) {

    fun findByClinicCodeAndPhoneNumber(clinicCode: String, phoneNumber: String): PatientEntity? {
        return patientRepository.findByClinicCodeAndPhoneNumber(clinicCode, phoneNumber)
    }

    fun save(patientEntity: PatientEntity): PatientEntity {
        return patientRepository.save(patientEntity)
    }
}
