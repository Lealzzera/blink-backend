package com.blink.backend.domain.integration.n8n.dto

import com.blink.backend.persistence.entity.appointment.AppointmentEntity
import java.time.LocalDateTime

data class MessageReceived(
    val message: String,
    val sender: String,
    val patientName: String,
    val appointmentsData: List<AppointmentsData>,
    val clinicCode: String,
    val clinicName: String,
    val aiName: String,
    val sentAt: LocalDateTime,
) {
    data class AppointmentsData(
        val appointmentId: Int,
        val scheduledTime: LocalDateTime,
        val appointmentDuration: Int,
        val appointmentStatus: String
    ) {
        companion object {
            fun fromAppointment(appointment: AppointmentEntity): AppointmentsData {
                return AppointmentsData(
                    appointmentId = appointment.id,
                    scheduledTime = appointment.scheduledTime,
                    appointmentDuration = appointment.duration,
                    appointmentStatus = appointment.appointmentStatus.name
                )
            }
        }
    }
}