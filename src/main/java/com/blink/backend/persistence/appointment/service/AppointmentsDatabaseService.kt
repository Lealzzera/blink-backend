package com.blink.backend.persistence.appointment.service

import com.blink.backend.domain.model.Appointment
import com.blink.backend.domain.model.Clinic
import com.blink.backend.persistence.entity.clinic.ClinicConfiguration

interface AppointmentsDatabaseService {
    fun saveAppointment(appointment: Appointment): Int

    fun getAppointmentsConfiguration(clinic: Clinic): ClinicConfiguration
}