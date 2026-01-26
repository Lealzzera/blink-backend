package com.blink.backend.controller.patient

import com.blink.backend.controller.patient.dto.CreatePatientRequest
import com.blink.backend.controller.patient.dto.PatientDto
import com.blink.backend.controller.patient.dto.UpdatePatientRequest
import com.blink.backend.domain.exception.NotFoundException
import com.blink.backend.domain.model.auth.AuthenticatedUser
import com.blink.backend.domain.patient.service.PatientsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("api/v2/patient")
@Tag(name = "Patients", description = "APIs para gerenciamento de pacientes")
class PatientsController(private val patientsService: PatientsService) {

    @PostMapping
    @Operation(summary = "Cria um novo paciente")
    @Throws(NotFoundException::class)
    fun createPatient(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestBody request: CreatePatientRequest
    ): ResponseEntity<Unit> {
        val patientCode = patientsService.createPatient(user.clinic, request)
        return ResponseEntity.created(URI.create(patientCode.toString())).build()
    }

    @GetMapping("phone-number/{phone}")
    @Operation(summary = "Busca paciente pelo número de telefone")
    @Throws(NotFoundException::class)
    fun getPatientByPhone(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable phone: String
    ): ResponseEntity<PatientDto> {
        return ResponseEntity.ok(patientsService.findByClinicCodeAndPhoneNumber(user.clinic.code, phone))
    }

    @GetMapping("{code}")
    @Operation(summary = "Busca paciente pelo código")
    @Throws(NotFoundException::class)
    fun getPatientByCode(@PathVariable code: UUID): ResponseEntity<PatientDto> {
        return ResponseEntity.ok(patientsService.getPatientByCode(code))
    }

    @PutMapping("{code}")
    @Operation(summary = "Atualiza os dados do paciente")
    @Throws(NotFoundException::class)
    fun updatePatient(
        @PathVariable code: UUID,
        @RequestBody request: UpdatePatientRequest
    ): ResponseEntity<Unit> {
        patientsService.updatePatient(code, request)
        return ResponseEntity.noContent().build()
    }
}
