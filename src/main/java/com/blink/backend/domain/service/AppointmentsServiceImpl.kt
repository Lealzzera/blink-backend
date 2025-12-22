package com.blink.backend.domain.service

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO
import com.blink.backend.domain.exception.NotFoundException
import com.blink.backend.domain.model.Appointment
import com.blink.backend.domain.model.Clinic
import com.blink.backend.domain.model.WorkdayAvailability
import com.blink.backend.persistence.entity.appointment.AppointmentEntity
import com.blink.backend.persistence.entity.appointment.ClinicAvailability
import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException
import com.blink.backend.persistence.entity.appointment.WeekDay
import com.blink.backend.persistence.repository.AppointmentsRepository
import com.blink.backend.persistence.repository.ClinicAvailabilityExceptionRepository
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository
import com.blink.backend.persistence.service.AppointmentsDatabaseService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.Optional

@Service
class AppointmentsServiceImpl(
    val clinicAvailabilityRepository: ClinicAvailabilityRepository,
    val appointmentsRepository: AppointmentsRepository,
    val clinicAvailabilityExceptionRepository: ClinicAvailabilityExceptionRepository,
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
        id: Int,
        appointment: Appointment
    ): Appointment {
        val appointmentEntity = appointmentsRepository.findById(id)
            .orElseThrow({ NotFoundException("Agendamento $id") })

        appointment.notes?.let { appointmentEntity.notes = it }
        appointment.scheduledTime?.let { appointmentEntity.scheduledTime = it }
        appointment.status?.let { appointmentEntity.appointmentStatus = it }

        appointmentsRepository.save(appointmentEntity)
        return appointment
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
        /*.map<ClinicAvailabilityDTO?> { date: LocalDate -> this.fromEntity(clinic.code, date, hideCancelled) }
        .filter { obj: ClinicAvailabilityDTO? -> Objects.nonNull(obj) }
        .toList()
        .filterNotNull()*/
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

    private fun fromEntity(clinicCode: String, date: LocalDate, hideCancelled: Boolean): ClinicAvailabilityDTO? {
        val clinicAvailabilityException: Optional<ClinicAvailabilityException?> = clinicAvailabilityExceptionRepository
            .findByExceptionDayAndClinicCode(date, clinicCode)

        val clinicAvailability: ClinicAvailability? = clinicAvailabilityRepository
            .findByWeekDayAndIsWorkingDayTrueAndClinicCode(WeekDay.fromDate(date), clinicCode)
        var appointments: MutableList<AppointmentEntity?> =
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

        val clinicAvailabilityDTO: ClinicAvailabilityDTO?

        if (clinicAvailabilityException.isEmpty()) {
            clinicAvailabilityDTO = ClinicAvailabilityDTO.fromEntity(date, clinicAvailability, appointments)
        } else {
            clinicAvailabilityDTO =
                ClinicAvailabilityDTO.fromException(date, clinicAvailabilityException.get(), appointments)
        }

        return clinicAvailabilityDTO
    }
}