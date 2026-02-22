package com.blink.backend.domain.patient.service

import com.blink.backend.controller.patient.dto.CreatePatientRequest
import com.blink.backend.controller.patient.dto.PatientDto
import com.blink.backend.controller.patient.dto.UpdatePatientRequest
import com.blink.backend.persistence.entity.clinic.ClinicEntity
import java.util.UUID

interface PatientsService {
    fun createPatient(clinic: ClinicEntity, request: CreatePatientRequest): UUID
    fun findByClinicCodeAndPhoneNumber(clinicCode: String, phoneNumber: String): PatientDto
    fun getPatientByCode(code: UUID): PatientDto
    fun updatePatient(code: UUID, request: UpdatePatientRequest)
    fun updatePatientByPhoneNumber(clinicCode: String, phoneNumber: String, request: UpdatePatientRequest)
}
