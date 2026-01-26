package com.blink.backend.domain.patient.service

import com.blink.backend.controller.patient.dto.CreatePatientRequest
import com.blink.backend.controller.patient.dto.PatientDto
import com.blink.backend.domain.exception.NotFoundException
import com.blink.backend.persistence.entity.appointment.PatientEntity
import com.blink.backend.persistence.entity.clinic.ClinicEntity
import com.blink.backend.persistence.patient.PatientRepositoryKt
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PatientsServiceImpl(
    private val patientRepository: PatientRepositoryKt
) : PatientsService {

    override fun createPatient(clinic: ClinicEntity, request: CreatePatientRequest): UUID {
        val patient = patientRepository.findByClinicCodeAndPhoneNumber(clinic.code, request.phoneNumber)
            ?: PatientEntity.builder()
                .clinic(clinic)
                .phoneNumber(request.phoneNumber)
                .name(request.name)
                .code(UUID.randomUUID())
                .build()

        return patientRepository.save(patient).code
    }

    override fun findByClinicCodeAndPhoneNumber(clinicCode: String, phoneNumber: String): PatientDto {
        val patient = patientRepository.findByClinicCodeAndPhoneNumber(clinicCode, phoneNumber)
            ?: throw NotFoundException("Paciente")

        return PatientDto.fromEntity(patient)
    }

    override fun getPatientByCode(code: UUID): PatientDto {
        val patient = patientRepository.findByCode(code)
            ?: throw NotFoundException("Paciente")

        return PatientDto.fromEntity(patient)
    }

    override fun updatePatientName(code: UUID, name: String) {
        val patient = patientRepository.findByCode(code)
            ?: throw NotFoundException("Paciente")

        patient.name = name
        patientRepository.save(patient)
    }
}
