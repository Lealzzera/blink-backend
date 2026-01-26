package com.blink.backend.domain.patient.service

import com.blink.backend.controller.patient.dto.CreatePatientRequest
import com.blink.backend.controller.patient.dto.PatientDto
import com.blink.backend.persistence.entity.clinic.ClinicEntity
import java.util.UUID

interface PatientsService {
    fun createPatient(clinic: ClinicEntity, request: CreatePatientRequest): UUID
    fun findByClinicCodeAndPhoneNumber(clinicCode: String, phoneNumber: String): PatientDto
    fun getPatientByCode(code: UUID): PatientDto
    fun updatePatientName(code: UUID, name: String)
}
