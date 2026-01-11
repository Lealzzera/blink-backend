package com.blink.backend.domain.appointment.service

import com.blink.backend.domain.exception.NotFoundException
import com.blink.backend.domain.exception.appointment.AppointmentConflictException
import com.blink.backend.domain.model.Appointment
import com.blink.backend.domain.model.AppointmentUpdate
import com.blink.backend.domain.model.Clinic
import com.blink.backend.domain.model.WorkdayAvailability
import com.blink.backend.persistence.entity.appointment.AppointmentEntity
import com.blink.backend.persistence.repository.AppointmentsRepository
import com.blink.backend.persistence.appointment.service.AppointmentsDatabaseService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AppointmentsServiceImpl(
    val appointmentsRepository: AppointmentsRepository,
    val availabilityService: AvailabilityService,
    val appointmentsDatabaseService: AppointmentsDatabaseService
) : AppointmentsService {


    override fun createAppointment(
        appointment: Appointment
    ): String {
        val appointmentsConfiguration = appointmentsDatabaseService.getAppointmentsConfiguration(appointment.clinic)
        val appointment = appointment.updateAppointmentDuration(appointmentsConfiguration.appointmentDuration)

        availabilityService.validateAppointmentTimeWithWorkdayShift(
            startDateTime = appointment.scheduledTime!!,
            endDateTime = appointment.scheduledTimeEnd!!,
            clinic = appointment.clinic,
        )
        availabilityService.validateAppointmentTimeAvailability(
            startDateTime = appointment.scheduledTime,
            endDateTime = appointment.scheduledTimeEnd,
            clinic = appointment.clinic,
            maximumAppointments = appointmentsConfiguration.maximumOverbookingAppointments
        )

        return appointmentsDatabaseService.saveAppointment(appointment).toString()
    }

    override fun updateAppointment(
        clinic: Clinic,
        appointmentId: Int,
        appointment: AppointmentUpdate
    ): Appointment {
        val appointmentsConfiguration = appointmentsDatabaseService.getAppointmentsConfiguration(clinic)
        val appointmentEntity = appointmentsRepository.findById(appointmentId)
            .orElseThrow { NotFoundException("Agendamento $appointmentId") }

        if (appointmentEntity.clinic.code != clinic.code) {
            throw AppointmentConflictException(
                "Agendamento $appointmentId não pertence a clinica ${clinic.code}"
            )
        }

        appointment.notes?.let { appointmentEntity.notes = it }
        appointment.scheduledTime?.let {
            val scheduledTimeEnd = appointment.scheduledTime
                .plusMinutes(appointmentsConfiguration.appointmentDuration.toLong())

            availabilityService.validateAppointmentTimeWithWorkdayShift(
                startDateTime = it,
                endDateTime = scheduledTimeEnd,
                clinic = clinic,
            )
            availabilityService.validateAppointmentTimeAvailability(
                startDateTime = appointment.scheduledTime,
                endDateTime = scheduledTimeEnd,
                clinic = clinic,
                maximumAppointments = appointmentsConfiguration.maximumOverbookingAppointments
            )
            appointmentEntity.scheduledTime = it
            appointmentEntity.duration = appointmentsConfiguration.appointmentDuration
        }
        appointment.status?.let { appointmentEntity.appointmentStatus = it }

        return appointmentsRepository.save(appointmentEntity).toDomain()
    }

    override fun getScheduledAppointmentsOnDateRange(
        clinic: Clinic,
        startDate: LocalDate,
        endDate: LocalDate,
        hideCancelled: Boolean
    ): List<WorkdayAvailability> {
        return startDate
            .datesUntil(endDate.plusDays(1))
            .map { getAvailabilityForDate(clinic, date = it, hideCancelled = hideCancelled) }
            .toList()
    }

    private fun getAvailabilityForDate(clinic: Clinic, date: LocalDate, hideCancelled: Boolean): WorkdayAvailability {
        val availability = availabilityService.getAvailabilityForDate(clinic = clinic, date = date)
        if (!availability.isWorkDay) {
            return availability
        }

        var appointments: MutableList<AppointmentEntity> =
            appointmentsRepository
                .findByScheduledTimeBetween(
                    date.atStartOfDay(),
                    date.plusDays(1).atStartOfDay()
                )

        if (hideCancelled) {
            appointments = appointments.stream()
                .filter { obj: AppointmentEntity? -> obj!!.isNotCancelled }
                .toList()
        }

        return availability.copy(appointments = appointments.stream().map { it.toDomain() }.toList())
    }
}