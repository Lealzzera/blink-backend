package com.blink.backend.domain.service;

import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDTO;
import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.WeekDay;
import com.blink.backend.persistence.repository.ClinicAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationAvailabilityService {

    private final ClinicAvailabilityRepository clinicAvailabilityRepository;

    public void updateAvailabilityConfiguration(List<AvailabilityConfigurationDTO> updateAvailabilityConfiguration) {

        for (AvailabilityConfigurationDTO configuration : updateAvailabilityConfiguration) {
            ClinicAvailability clinicAvailability = clinicAvailabilityRepository
                    .findByClinicIdAndWeekDayAndIsWorkingDayTrue(
                            configuration.getClinicId(),
                            WeekDay.valueOf(configuration.getWeekDay()));

            clinicAvailability.setOpenTime(configuration.getOpen());
            clinicAvailability.setCloseTime(configuration.getClose());
            clinicAvailability.setLunchStartTime(configuration.getBreakStart());
            clinicAvailability.setLunchEndTime(configuration.getBreakEnd());

            clinicAvailabilityRepository.save(clinicAvailability);
        }


    }

}
