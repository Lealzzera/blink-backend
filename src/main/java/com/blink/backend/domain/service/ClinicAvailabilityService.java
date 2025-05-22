package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.AppointmentDTO;
import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.controller.appointment.dto.CreateAppointmentDTO;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.appointment.ServiceType;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.persistence.repository.AppointmentStatusRepository;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository;
import com.blink.backend.persistence.repository.ClinicRepository;
import com.blink.backend.persistence.repository.PatientRepository;
import com.blink.backend.persistence.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClinicAvailabilityService {
    private final ClinicAvailabilityRepository clinicAvailabilityRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final AppointmentStatusRepository appointmentStatusRepository;


    public List<ClinicAvailabilityDTO> getClinicAvailability(
            LocalDate startDate,
            LocalDate endDate) {

        return startDate
                .datesUntil(endDate.plusDays(1))
                .map(this::fromEntity)
                .filter(Objects::nonNull)
                .toList();
    }

    private ClinicAvailabilityDTO fromEntity(LocalDate date) {
        ClinicAvailability clinicAvailability = clinicAvailabilityRepository
                .findByWeekDayNameAndIsWorkingDayTrue(date.getDayOfWeek().name());
        List<Appointment> appointments = appointmentsRepository
                .findByScheduledTimeBetween(
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay());

        return ClinicAvailabilityDTO.fromEntity(date, clinicAvailability, appointments);
    }


    public void saveAppointment (CreateAppointmentDTO appointment){


        Patient patient = patientRepository.findByPhoneNumber(appointment.getPatientNumber());

        Clinic clinic = clinicRepository
                .findById(appointment.getClinicId()).orElse(null);
        ServiceType serviceType = serviceTypeRepository
                .findById(appointment.getServiceTypeId()).orElse(null);
        AppointmentStatus appointmentStatus = appointmentStatusRepository
                .findById(1).orElse(null);

        Appointment appointment1 = Appointment.builder()
                .patient(patient)
                .scheduledTime(appointment.getScheduledTime())
                .clinic(clinic)
                .serviceType(serviceType)
                .duration(30)
                .appointmentStatus(appointmentStatus)
                .notes(appointment.getNotes())
                .build();

        appointmentsRepository.save(appointment1);

    }

    public Appointment getAppointmentDetailsById(Integer id){
        Appointment appointment = appointmentsRepository.findById(id)
                .orElse(null);

        return appointment;

    }

    public void cancelAppointment (Integer id){

        Appointment appointment = appointmentsRepository.findById(id).orElse(null);
        AppointmentStatus canceledStatus = appointmentStatusRepository.findByStatusIgnoreCase("Cancelado");

        if (canceledStatus == null){

            throw new RuntimeException("Status 'Cancelado' não encontrado");

        }

        appointment.setAppointmentStatus(canceledStatus);
        appointmentsRepository.save(appointment);

    }


}
