package com.blink.backend.persistence.appointment.service

import com.blink.backend.domain.model.Appointment
import com.blink.backend.domain.model.Clinic
import com.blink.backend.persistence.entity.appointment.AppointmentEntity
import com.blink.backend.persistence.entity.appointment.AppointmentStatus
import com.blink.backend.persistence.entity.appointment.PatientEntity
import com.blink.backend.persistence.entity.clinic.ClinicConfiguration
import com.blink.backend.persistence.repository.AppointmentsRepository
import com.blink.backend.persistence.repository.ClinicConfigurationRepository
import com.blink.backend.persistence.repository.PatientRepository
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AppointmentsDatabaseServiceImpl(
    private val patientRepository: PatientRepository,
    private val clinicRepository: ClinicRepositoryService,
    private val clinicConfigurationRepository: ClinicConfigurationRepository,
    private val appointmentsRepository: AppointmentsRepository,
) : AppointmentsDatabaseService {
    companion object {
        val log = LoggerFactory.getLogger(AppointmentsDatabaseServiceImpl::class.java)!!
    }

    override fun saveAppointment(appointment: Appointment): Int {

        val clinicEntity = clinicRepository.findByCode(appointment.clinic.code)
        val clinicConfiguration = clinicConfigurationRepository.findByClinicId(clinicEntity.id)

        val patient = patientRepository
            .findByPhoneNumber(appointment.patient.phoneNumber.trim())
            .orElseGet {
                log.info("saving patient ${appointment.patient.phoneNumber} for clinic ${clinicEntity.code}")
                patientRepository.save(
                    PatientEntity.builder()
                        .phoneNumber(appointment.patient.phoneNumber.trim())
                        .name(appointment.patient.name.trim())
                        .createdAt(LocalDateTime.now())
                        .clinic(clinicEntity)
                        .aiAnswer(clinicConfiguration.defaultAiAnswer)
                        .build()
                )
            }


        val appointment = AppointmentEntity.builder()
            .patient(patient)
            .scheduledTime(appointment.scheduledTime)
            .clinic(clinicEntity)
            .duration(appointment.duration)
            .appointmentStatus(AppointmentStatus.AGENDADO)
            .notes(appointment.notes)
            .createdAt(LocalDateTime.now())
            .build()
        return appointmentsRepository.save(appointment).id
    }

    override fun getAppointmentsConfiguration(clinic: Clinic): ClinicConfiguration {
        val clinicEntity = clinicRepository.findByCode(clinic.code)
        return clinicConfigurationRepository.findByClinicId(clinicEntity.id)
    }
}