package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.AppointmentDetailsDTO;
import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.controller.appointment.dto.CreateAppointmentDTO;
import com.blink.backend.controller.appointment.dto.UpdateAppointmentDTO;
import com.blink.backend.controller.appointment.dto.UpdateAppointmentStatusDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.appointment.AppointmentConflictException;
import com.blink.backend.domain.model.Availability;
import com.blink.backend.persistence.entity.appointment.AppointmentEntity;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException;
import com.blink.backend.persistence.entity.appointment.PatientEntity;
import com.blink.backend.persistence.entity.appointment.WeekDay;
import com.blink.backend.persistence.entity.clinic.ClinicEntity;
import com.blink.backend.persistence.entity.clinic.ClinicConfiguration;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.ClinicAvailabilityExceptionRepository;
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository;
import com.blink.backend.persistence.repository.ClinicConfigurationRepository;
import com.blink.backend.persistence.repository.PatientRepository;
import com.blink.backend.persistence.repository.ServiceTypeRepository;
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflictReason.DURING_BREAK;
import static com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflictReason.OUTSIDE_WORK_DAY;
import static com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflictReason.OUTSIDE_WORK_HOURS;
import static com.blink.backend.domain.exception.appointment.AppointmentConflictException.AppointmentConflictReason.OVERLAP;
import static com.blink.backend.persistence.entity.appointment.AppointmentStatus.AGENDADO;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ClinicAvailabilityService {
    private final ClinicAvailabilityRepository clinicAvailabilityRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final ClinicRepositoryService clinicRepository;
    private final PatientRepository patientRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ClinicConfigurationRepository clinicConfigurationRepository;
    private final ClinicAvailabilityExceptionRepository clinicAvailabilityExceptionRepository;


    public List<ClinicAvailabilityDTO> getClinicAvailability(
            Integer clinicId,
            LocalDate startDate,
            LocalDate endDate,
            Boolean hideCancelled) {

        return startDate
                .datesUntil(endDate.plusDays(1))
                .map(date -> this.fromEntity(clinicId, date, hideCancelled))
                .filter(Objects::nonNull)
                .toList();
    }

    private ClinicAvailabilityDTO fromEntity(Integer clinicId, LocalDate date, Boolean hideCancelled) {
        Optional<ClinicAvailabilityException> clinicAvailabilityException = clinicAvailabilityExceptionRepository
                .findByExceptionDayAndClinicId(date, clinicId);

        ClinicAvailability clinicAvailability = clinicAvailabilityRepository
                .findByWeekDayAndIsWorkingDayTrueAndClinicId(WeekDay.fromDate(date), clinicId);
        List<AppointmentEntity> appointments = appointmentsRepository
                .findByScheduledTimeBetween(
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay());

        if (hideCancelled) {
            appointments = appointments.stream()
                    .filter(AppointmentEntity::isNotCancelled)
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


    public AppointmentEntity saveAppointment(CreateAppointmentDTO appointmentRequest)
            throws NotFoundException, AppointmentConflictException {

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
                .findByExceptionDayAndClinicId(appointmentRequest.getScheduledTime().toLocalDate(),
                        appointmentRequest.getClinicId());
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

        PatientEntity patient = patientRepository
                .findByPhoneNumber(appointmentRequest.getPatientNumber().trim())
                .orElseThrow(() -> new NotFoundException("Paciente"));
        ClinicEntity clinic = clinicRepository
                .findById(appointmentRequest.getClinicId());

        AppointmentEntity appointment = AppointmentEntity.builder()
                .patient(patient)
                .scheduledTime(appointmentRequest.getScheduledTime())
                .clinic(clinic)
                .duration(appointmentDuration)
                .appointmentStatus(AGENDADO)
                .notes(appointmentRequest.getNotes())
                .createdAt(LocalDateTime.now())
                .build();

        return appointmentsRepository.save(appointment);

    }

    public AppointmentDetailsDTO getAppointmentDetailsById(Integer id) throws NotFoundException {
        return appointmentsRepository.findById(id)
                .map(AppointmentDetailsDTO::fromEntity)
                .orElseThrow(() -> new NotFoundException("Agendamento " + id));
    }

    public void updateAppointmentStatus(UpdateAppointmentStatusDTO updateStatus) throws NotFoundException {
        AppointmentEntity appointment = appointmentsRepository.findById(updateStatus.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Agendamento " + updateStatus.getAppointmentId()));

        appointment.setAppointmentStatus(AppointmentStatus.valueOf(updateStatus.getNewStatus().toUpperCase()));
        appointmentsRepository.save(appointment);
    }

    public void updateAppointment(Integer appointmentId, UpdateAppointmentDTO updateAppointmentDTO) throws NotFoundException {
        AppointmentEntity appointment = appointmentsRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Agendamento " + appointmentId));

        if (Objects.nonNull(updateAppointmentDTO.getNotes())) {
            appointment.setNotes(updateAppointmentDTO.getNotes());
        }

        if (Objects.nonNull(updateAppointmentDTO.getScheduledTime())) {
            appointment.setScheduledTime(updateAppointmentDTO.getScheduledTime());
        }

        if (Objects.nonNull(updateAppointmentDTO.getStatus())) {
            appointment.setAppointmentStatus(updateAppointmentDTO.getStatus());
        }

        appointmentsRepository.save(appointment);
    }


}
