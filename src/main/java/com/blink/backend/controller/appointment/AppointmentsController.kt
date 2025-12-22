package com.blink.backend.controller.appointment

import com.blink.backend.controller.appointment.dto.CreateAppointmentsDTO
import com.blink.backend.domain.model.auth.AuthenticatedUser
import com.blink.backend.domain.service.AppointmentsService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/appointments")
class AppointmentsController(
    private val appointmentsService: AppointmentsService
) {

    @PostMapping
    fun createAppointment(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestBody createAppointmentDTO: CreateAppointmentsDTO
    ): ResponseEntity<String> {
        val appointmentId =
            appointmentsService.createAppointment(createAppointmentDTO.toAppointment(user.clinic.toDomain()))
        return ResponseEntity.created(URI.create("/api/v1/appointments/${appointmentId}/details")).build()
    }

    /*@GetMapping("availability")
    fun getClinicAvailabilityByClinicId(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestParam("start_date") startDate: LocalDate,
        @RequestParam(value = "end_date", required = false) endDate: LocalDate?,
        @RequestParam(value = "hide_cancelled", required = false, defaultValue = "true") hideCancelled: Boolean?
    ): ResponseEntity<List<ClinicAvailabilityDTO?>?> {
        var endDate = endDate
        endDate = if (Objects.isNull(endDate)) startDate.plusDays(7) else endDate

        return ResponseEntity.ok<List<ClinicAvailabilityDTO?>?>(
            appointmentsService.getScheduledAppointmentsOnDateRange(
                user.clinic.toDomain(),
                startDate,
                endDate!!,
                hideCancelled!!
            )
        )
    }

    @PutMapping("{appointmentId}")
    @Throws(NotFoundException::class)
    fun updateAppointment(
        @PathVariable appointmentId: String,
        @RequestBody updateAppointmentDTO: UpdateAppointmentDTO
    ): ResponseEntity<Void?> {
        appointmentsService.updateAppointment(appointmentId, updateAppointmentDTO.toDomain())
        return ResponseEntity.noContent().build<Void?>()
    }*/
}