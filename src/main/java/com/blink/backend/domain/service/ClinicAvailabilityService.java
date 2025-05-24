package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.controller.appointment.dto.CreateAppointmentDTO;
import com.blink.backend.controller.appointment.dto.UpdateAppointmentStatusDTO;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.appointment.ServiceType;
import com.blink.backend.persistence.entity.clinic.Clinic;
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
import java.util.Optional;

import static com.blink.backend.persistence.entity.appointment.AppointmentStatus.AGENDADO;
import static com.blink.backend.persistence.entity.appointment.AppointmentStatus.CANCELADO;

@Service
@RequiredArgsConstructor
public class ClinicAvailabilityService {
    private final ClinicAvailabilityRepository clinicAvailabilityRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final ServiceTypeRepository serviceTypeRepository;


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
                .findByScheduledTimeBetweenAndAppointmentStatusIsNot(
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay(),
                        CANCELADO);

        return ClinicAvailabilityDTO.fromEntity(date, clinicAvailability, appointments);
    }


    public Appointment saveAppointment(CreateAppointmentDTO appointment) {


        Patient patient = patientRepository.findByPhoneNumber(appointment.getPatientNumber());

        Clinic clinic = clinicRepository
                .findById(appointment.getClinicId()).orElse(null);
        ServiceType serviceType = serviceTypeRepository
                .findById(appointment.getServiceTypeId()).orElse(null);

        Appointment appointment1 = Appointment.builder()
                .patient(patient)
                .scheduledTime(appointment.getScheduledTime())
                .clinic(clinic)
                .serviceType(serviceType)
                .duration(30)
                .appointmentStatus(AGENDADO)
                .notes(appointment.getNotes())
                .build();

        return appointmentsRepository.save(appointment1);

    }

    public Appointment getAppointmentDetailsById(Integer id) {
        return appointmentsRepository.findById(id)
                .orElse(null);
    }

    public void cancelAppointment(Integer id) {
        Optional<Appointment> appointmentOptional = appointmentsRepository.findById(id);
        if (appointmentOptional.isEmpty()) {
            throw new RuntimeException("Agendamento não encontrado");
        }

        appointmentOptional.get().setAppointmentStatus(CANCELADO);
        appointmentsRepository.save(appointmentOptional.get());
    }

    public void updateAppointmentStatus(UpdateAppointmentStatusDTO updateStatus) {

        Optional<Appointment> appointmentOptional = appointmentsRepository.findById(updateStatus.getAppointmentId());

        if (appointmentOptional.isEmpty()) {
            throw new RuntimeException("Agendamento não encontrado");
        }

        appointmentOptional.get().setAppointmentStatus(AppointmentStatus.valueOf(updateStatus.getNewStatus().toUpperCase()));
        appointmentsRepository.save(appointmentOptional.get());


    }


}
