package com.blink.backend.domain.service;

import com.blink.backend.persistence.entity.appointment.*;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.controller.appointment.dto.CreateAppointmentDTO;
import com.blink.backend.persistence.repository.*;
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

        Patient patient = patientRepository.findById(1).orElse(null);
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
                .appointmentStatus(appointmentStatus) // Criar objeto com objeto de agendado.
                .notes(appointment.getNotes())
                .build();

        appointmentsRepository.save(appointment1);

    }

}
