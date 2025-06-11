package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.AppointmentDetailsDTO;
import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.controller.appointment.dto.CreateAppointmentDTO;
import com.blink.backend.controller.appointment.dto.UpdateAppointmentStatusDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.appointment.AppointmentConflictException;
import com.blink.backend.domain.model.Availability;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.appointment.ServiceType;
import com.blink.backend.persistence.entity.appointment.WeekDay;
import com.blink.backend.persistence.entity.clinic.Clinic;
import com.blink.backend.persistence.entity.clinic.ClinicConfiguration;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.ClinicAvailabilityExceptionRepository;
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository;
import com.blink.backend.persistence.repository.ClinicConfigurationRepository;
import com.blink.backend.persistence.repository.ClinicRepository;
import com.blink.backend.persistence.repository.PatientRepository;
import com.blink.backend.persistence.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflitReason.DURING_BREAK;
import static com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflitReason.OUTSIDE_WORK_DAY;
import static com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflitReason.OUTSIDE_WORK_HOURS;
import static com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflitReason.OVERLAP;
import static com.blink.backend.persistence.entity.appointment.AppointmentStatus.AGENDADO;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ClinicAvailabilityService {
    private final ClinicAvailabilityRepository clinicAvailabilityRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ClinicConfigurationRepository clinicConfigurationRepository;
    private final ClinicAvailabilityExceptionRepository clinicAvailabilityExceptionRepository;


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
        Optional<ClinicAvailabilityException> clinicAvailabilityException = clinicAvailabilityExceptionRepository
                .findByExceptionDay(date);

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

        ClinicAvailabilityDTO clinicAvailabilityDTO;

        if (clinicAvailabilityException.isEmpty()) {
            clinicAvailabilityDTO = ClinicAvailabilityDTO.fromEntity(date, clinicAvailability, appointments);

        } else {
            clinicAvailabilityDTO = ClinicAvailabilityDTO.fromException(date, clinicAvailabilityException.get(), appointments);
        }

        return clinicAvailabilityDTO;

    }


    public Appointment saveAppointment(CreateAppointmentDTO appointmentRequest) {

        ClinicConfiguration clinicConfiguration = clinicConfigurationRepository
                .findByClinicId(appointmentRequest.getClinicId());
        Integer appointmentDuration = clinicConfiguration.getAppointmentDuration();
        Integer countAppointments = appointmentsRepository
                .countByScheduledTimeBetweenAndAppointmentStatusNot(
                        appointmentRequest.getScheduledTime(),
                        appointmentRequest.getScheduledTimeEnd(appointmentDuration),
                        AppointmentStatus.CANCELADO);

        if (countAppointments >= clinicConfiguration.getMaximumOverbookingAppointments()) {
            throw new AppointmentConflictException(OVERLAP);
        }

        Optional<ClinicAvailabilityException> exception = clinicAvailabilityExceptionRepository
                .findByExceptionDay(appointmentRequest.getScheduledTime().toLocalDate());
        Availability availability;
        if (exception.isEmpty()) {
            ClinicAvailability clinicAvailability = clinicAvailabilityRepository
                    .findByClinicIdAndWeekDayAndIsWorkingDayTrue(appointmentRequest.getClinicId(),
                            WeekDay.fromDate(appointmentRequest.getScheduledTime().toLocalDate()))
                    .orElseThrow(() -> new AppointmentConflictException(OUTSIDE_WORK_DAY));
            availability = Availability.fromClinicAvailability(clinicAvailability);
        } else {
            availability = Availability.fromClinicAvailabilityException(exception.get());
        }

        if (!availability.getIsWorkingDay()) {
            throw new AppointmentConflictException(OUTSIDE_WORK_DAY);
        }

        if (appointmentRequest.getScheduledTime().toLocalTime().isBefore(availability.getOpenTime()) ||
                appointmentRequest.getScheduledTimeEnd(appointmentDuration).toLocalTime().isAfter(availability.getCloseTime())) {
            throw new AppointmentConflictException(OUTSIDE_WORK_HOURS);
        }

        if (!isNull(availability.getLunchStartTime()) &&
                !isNull(availability.getLunchEndTime()) &&
                appointmentRequest.getScheduledTimeEnd(appointmentDuration).toLocalTime().isAfter(availability.getLunchStartTime()) &&
                appointmentRequest.getScheduledTime().toLocalTime().isBefore(availability.getLunchEndTime())) {
            throw new AppointmentConflictException(DURING_BREAK);
        }

        Patient patient = patientRepository
                .findByPhoneNumber(appointmentRequest.getPatientNumber())
                .orElseThrow(() -> new NotFoundException("Paciente"));
        Clinic clinic = clinicRepository
                .findById(appointmentRequest.getClinicId())
                .orElseThrow(() -> new NotFoundException("Clinica"));
        ServiceType serviceType = serviceTypeRepository
                .findById(appointmentRequest.getServiceTypeId())
                .orElseThrow(() -> new NotFoundException("Tipo de serviço"));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .scheduledTime(appointmentRequest.getScheduledTime())
                .clinic(clinic)
                .serviceType(serviceType)
                .duration(appointmentDuration)
                .appointmentStatus(AGENDADO)
                .notes(appointmentRequest.getNotes())
                .build();

        return appointmentsRepository.save(appointment);

    }

    public AppointmentDetailsDTO getAppointmentDetailsById(Integer id) {
        return appointmentsRepository.findById(id)
                .map(AppointmentDetailsDTO::fromEntity)
                .orElseThrow(() -> new NotFoundException("Agendamento " + id));
    }

    public void updateAppointmentStatus(UpdateAppointmentStatusDTO updateStatus) {
        Appointment appointment = appointmentsRepository.findById(updateStatus.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Agendamento " + updateStatus.getAppointmentId()));

        appointment.setAppointmentStatus(AppointmentStatus.valueOf(updateStatus.getNewStatus().toUpperCase()));
        appointmentsRepository.save(appointment);
    }


}
