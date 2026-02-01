package com.blink.backend.controller.configuration

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityExceptionDTO
import com.blink.backend.domain.exception.NotFoundException
import com.blink.backend.domain.model.auth.AuthenticatedUser
import com.blink.backend.domain.service.ClinicAvailabilityExceptionService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("api/v2/configuration/availability/atypical")
class AtypicalWorkdayConfigurationController(
    private val clinicAvailabilityExceptionService: ClinicAvailabilityExceptionService
) {
    @PostMapping
    @Throws(NotFoundException::class)
    fun createAvailabilityException(
        @RequestBody availabilityExceptionDTO: ClinicAvailabilityExceptionDTO
    ): ResponseEntity<Int> {
        val uri = URI.create(
            clinicAvailabilityExceptionService.createAvailabilityException(availabilityExceptionDTO).toString()
        )
        return ResponseEntity.created(uri).build()
    }

    @GetMapping
    fun getClinicAvailabilityException(
        @AuthenticationPrincipal user: AuthenticatedUser
    ): ResponseEntity<List<ClinicAvailabilityExceptionDTO>> {
        return ResponseEntity.ok(
            clinicAvailabilityExceptionService.getClinicAvailabilityExceptionByClinic(user.clinic.id)
        )
    }

    @GetMapping("{id}")
    @Throws(NotFoundException::class)
    fun getClinicAtypicalAvailabilityById(
        @PathVariable id: Int
    ): ResponseEntity<ClinicAvailabilityExceptionDTO> {
        return ResponseEntity.ok(
            clinicAvailabilityExceptionService.getClinicAvailabilityExceptionById(id)
        )
    }

    @DeleteMapping("{atypicalId}")
    @Throws(NotFoundException::class)
    fun deleteAvailabilityException(
        @PathVariable atypicalId: Int
    ): ResponseEntity<Unit> {
        clinicAvailabilityExceptionService.deleteClinicAvailabilityExceptionById(atypicalId)
        return ResponseEntity.noContent().build()
    }
}