package com.blink.backend.domain.service

import com.blink.backend.domain.exception.appointment.AppointmentConflictException
import com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflitReason
import com.blink.backend.domain.model.Availability
import com.blink.backend.domain.model.Clinic
import com.blink.backend.persistence.entity.appointment.AppointmentStatus
import com.blink.backend.persistence.entity.appointment.WeekDay
import com.blink.backend.persistence.repository.AppointmentsRepository
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository
import com.blink.backend.persistence.service.AtypicalWorkdayDatabaseService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AvailabilityServiceImpl(
    private val atypicalWorkdayDatabaseService: AtypicalWorkdayDatabaseService,
    private val clinicAvailabilityDatabaseService: ClinicAvailabilityRepository,
    private val appointmentsRepository: AppointmentsRepository,
) : AvailabilityService {
    override fun validateAppointmentTimeWithWorkdayShift(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        clinic: Clinic,
    ) {
        val exception = atypicalWorkdayDatabaseService.findByDayAndClinic(
            scheduledDay = startDateTime.toLocalDate(),
            clinic = clinic,
        )
        val availability = exception?.let {
            Availability.fromClinicAvailabilityException(it)
        } ?: run {
            val clinicAvailability = clinicAvailabilityDatabaseService
                .findByClinicCodeAndWeekDayAndIsWorkingDayTrue(
                    clinic.code,
                    WeekDay.fromDate(startDateTime.toLocalDate())
                )
                .orElseThrow { AppointmentConflictException(AppointmentConflitReason.OUTSIDE_WORK_DAY) }
            Availability.fromClinicAvailability(clinicAvailability)
        }

        //checks
        if (!availability.isWorkingDay) {
            throw AppointmentConflictException(AppointmentConflitReason.OUTSIDE_WORK_DAY)
        }

        if (startDateTime.toLocalTime().isBefore(availability.openTime) ||
            endDateTime.toLocalTime().isAfter(availability.closeTime)
        ) {
            throw AppointmentConflictException(AppointmentConflitReason.OUTSIDE_WORK_HOURS)
        }

        if ((availability.lunchStartTime?.isBefore(endDateTime.toLocalTime()) == true &&
                    availability.lunchEndTime?.isAfter(endDateTime.toLocalTime()) == true) ||
            (availability.lunchStartTime?.isBefore(startDateTime.toLocalTime()) == true &&
                    availability.lunchEndTime?.isAfter(startDateTime.toLocalTime()) == true)
        ) {
            throw AppointmentConflictException(AppointmentConflitReason.DURING_BREAK)
        }
    }

    override fun validateAppointmentTimeAvailability(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        clinic: Clinic,
        maximumAppointments: Int
    ) {

        val countAppointments = appointmentsRepository
            .countByScheduledTimeBetweenAndAppointmentStatusNot(
                startDateTime,
                endDateTime,
                AppointmentStatus.CANCELADO
            )

        if (countAppointments >= maximumAppointments) {
            throw AppointmentConflictException(AppointmentConflitReason.OVERLAP)
        }
    }
}