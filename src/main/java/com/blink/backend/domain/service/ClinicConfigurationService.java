package com.blink.backend.domain.service;

import com.blink.backend.controller.configuration.dto.AppointmentConfigurationDTO;
import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDTO;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.WeekDay;
import com.blink.backend.persistence.entity.clinic.ClinicConfiguration;
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository;
import com.blink.backend.persistence.repository.ClinicConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicConfigurationService {
    private final ClinicAvailabilityRepository clinicAvailabilityRepository;
    private final ClinicConfigurationRepository clinicConfigurationRepository;

    public List<AvailabilityConfigurationDTO> getAvailabilityConfiguration(Integer clinicId) {
        return clinicAvailabilityRepository
                .findByClinicId(clinicId)
                .stream()
                .map(AvailabilityConfigurationDTO::fromEntity)
                .toList();
    }

    public void updateAvailabilityConfiguration(List<AvailabilityConfigurationDTO> updateAvailabilityConfiguration) {

        for (AvailabilityConfigurationDTO configuration : updateAvailabilityConfiguration) {
            ClinicAvailability clinicAvailability = clinicAvailabilityRepository
                    .findByClinicIdAndWeekDay(
                            configuration.getClinicId(),
                            WeekDay.valueOf(configuration.getWeekDay()));

            clinicAvailability.setOpenTime(configuration.getOpen());
            clinicAvailability.setCloseTime(configuration.getClose());
            clinicAvailability.setLunchStartTime(configuration.getBreakStart());
            clinicAvailability.setLunchEndTime(configuration.getBreakEnd());
            clinicAvailability.setIsWorkingDay(configuration.getWorkDay());

            clinicAvailabilityRepository.save(clinicAvailability);
        }

    }

    public void updateAppointmentConfiguration(AppointmentConfigurationDTO appointmentConfiguration) {

        ClinicConfiguration clinicConfiguration = clinicConfigurationRepository
                .findByClinicId(appointmentConfiguration.getClinicId());

        if (appointmentConfiguration.getDuration() != null)
            clinicConfiguration.setAppointmentDuration(appointmentConfiguration.getDuration());

        if (appointmentConfiguration.getOverbooking() != null)
            clinicConfiguration.setAllowOverbooking(appointmentConfiguration.getOverbooking());

        clinicConfigurationRepository.save(clinicConfiguration);

    }

    public AppointmentConfigurationDTO getAppointmentConfiguration(Integer clinicId) {

        ClinicConfiguration clinicConfiguration = clinicConfigurationRepository
                .findByClinicId(clinicId);

        return AppointmentConfigurationDTO
                .builder()
                .clinicId(clinicId)
                .duration(clinicConfiguration.getAppointmentDuration())
                .overbooking(clinicConfiguration.getAllowOverbooking())
                .build();
    }


}
