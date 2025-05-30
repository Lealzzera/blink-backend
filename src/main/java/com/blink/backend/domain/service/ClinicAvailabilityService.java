package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.controller.appointment.dto.CreateAppointmentDTO;
import com.blink.backend.controller.appointment.dto.UpdateAppointmentStatusDTO;
import com.blink.backend.persistence.entity.appointment.*;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.persistence.entity.clinic.ClinicConfiguration;
import com.blink.backend.persistence.repository.*;
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
    private final ClinicConfigurationRepository clinicConfigurationRepository;


    public List<ClinicAvailabilityDTO> getClinicAvailability(
            LocalDate startDate,
            LocalDate endDate,
            Boolean hideCancelled) {

        return startDate
                .datesUntil(endDate.plusDays(1))
                .map(date -> this.fromEntity(date, hideCancelled))
                .filter(Objects::nonNull)
                .toList();
    }

    private ClinicAvailabilityDTO fromEntity(LocalDate date, Boolean hideCancelled) {
        ClinicAvailability clinicAvailability = clinicAvailabilityRepository
                .findByWeekDayAndIsWorkingDayTrue(WeekDay.fromDate(date));
        List<Appointment> appointments = appointmentsRepository
                .findByScheduledTimeBetween(
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay());

        if (hideCancelled) {
            appointments = appointments.stream()
                    .filter(Appointment::isNotCancelled)
                    .toList();
        }

        return ClinicAvailabilityDTO.fromEntity(date, clinicAvailability, appointments);
    }


    public Appointment saveAppointment(CreateAppointmentDTO appointment) {

        ClinicConfiguration clinicConfiguration = clinicConfigurationRepository
                .findByClinicId(appointment.getClinicId());
        Patient patient = patientRepository
                .findByPhoneNumber(appointment.getPatientNumber());

        Clinic clinic = clinicRepository
                .findById(appointment.getClinicId()).orElse(null);
        ServiceType serviceType = serviceTypeRepository
                .findById(appointment.getServiceTypeId()).orElse(null);

        Appointment appointment1 = Appointment.builder()
                .patient(patient)
                .scheduledTime(appointment.getScheduledTime())
                .clinic(clinic)
                .serviceType(serviceType)
                .duration(clinicConfiguration.getAppointmentDuration())
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
