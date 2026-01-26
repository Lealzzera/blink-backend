package com.blink.backend.controller.patient.dto

import com.blink.backend.persistence.entity.appointment.PatientEntity
import java.util.UUID

data class PatientDto(
    val code: UUID,
    val name: String,
    val phoneNumber: String,
    val clinicCode: String
) {
    companion object {
        fun fromEntity(patient: PatientEntity) = PatientDto(
            code = patient.code,
            name = patient.name,
            phoneNumber = patient.phoneNumber,
            clinicCode = patient.clinic.code
        )
    }
}
