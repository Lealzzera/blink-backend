package com.blink.backend.controller.appointment

import com.blink.backend.controller.appointment.dto.AvailabilityDTO
import com.blink.backend.controller.appointment.dto.AvailabilityDTO.Companion.fromDomain
import com.blink.backend.controller.appointment.dto.CreateAppointmentsDTO
import com.blink.backend.controller.appointment.dto.UpdateAppointmentDto
import com.blink.backend.domain.appointment.service.AppointmentsService
import com.blink.backend.domain.model.auth.AuthenticatedUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.time.LocalDate
import java.util.logging.Logger

@RestController
@RequestMapping("api/v2/appointments")
@Tag(name = "Appointments", description = "API para gerenciamento de agendamentos")
class AppointmentsController(
    private val appointmentsService: AppointmentsService,
    private val logger: Logger = Logger.getLogger(AppointmentsController::class.java.name)
) {

    @PostMapping
    @Operation(summary = "Cria um novo agendamento")
    fun createAppointment(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestBody createAppointmentDTO: CreateAppointmentsDTO
    ): ResponseEntity<String> {
        val appointmentId =
            appointmentsService.createAppointment(createAppointmentDTO.toAppointment(user.clinic.toDomain()))
        return ResponseEntity.created(URI.create("/api/v1/appointments/${appointmentId}/details")).build()
    }

    @GetMapping("availability")
    @Operation(summary = "Retorna a disponibilidade de agendamentos da clínica em um intervalo de datas")
    fun getClinicAvailabilityByClinicId(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestParam("start_date", required = false) startDate: LocalDate?,
        @RequestParam(value = "end_date", required = false) endDate: LocalDate?,
        @RequestParam(value = "hide_cancelled", required = false, defaultValue = "true") hideCancelled: Boolean?
    ): ResponseEntity<List<AvailabilityDTO>> {
        val startDate: LocalDate = startDate ?: run {
            logger.info { "stage=setting-default-start-date" }
            LocalDate.now()
        }
        val endDate: LocalDate = endDate ?: startDate.plusDays(7)
        return ResponseEntity.ok(
            appointmentsService.getScheduledAppointmentsOnDateRange(
                user.clinic.toDomain(),
                startDate,
                endDate,
                hideCancelled!!
            )
                .stream()
                .map { fromDomain(it) }
                .toList()
        )
    }

    @PatchMapping("{appointmentId}")
    @Operation(summary = "Atualiza um agendamento existente")
    fun updateAppointment(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable appointmentId: Int,
        @RequestBody updateAppointmentDto: UpdateAppointmentDto
    ): ResponseEntity<Unit> {
        appointmentsService.updateAppointment(
            clinic = user.clinic.toDomain(),
            appointmentId = appointmentId,
            appointment = updateAppointmentDto.toDomain()
        )
        return ResponseEntity.ok().build()
    }
}