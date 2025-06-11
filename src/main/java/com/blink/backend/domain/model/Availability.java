package com.blink.backend.domain.model;

import com.blink.backend.persistence.entity.appointment.ClinicAvailability;
import com.blink.backend.persistence.entity.appointment.ClinicAvailabilityException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Availability {
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final LocalTime lunchStartTime;
    private final LocalTime lunchEndTime;
    private final Boolean isWorkingDay;

    public static Availability fromClinicAvailability(ClinicAvailability clinicAvailability) {
        return Availability.builder()
                .openTime(clinicAvailability.getOpenTime())
                .closeTime(clinicAvailability.getCloseTime())
                .lunchStartTime(clinicAvailability.getLunchStartTime())
                .lunchEndTime(clinicAvailability.getLunchEndTime())
                .isWorkingDay(clinicAvailability.getIsWorkingDay())
                .build();
    }

    public static Availability fromClinicAvailabilityException(ClinicAvailabilityException clinicAvailability) {
        return Availability.builder()
                .openTime(clinicAvailability.getOpenTime())
                .closeTime(clinicAvailability.getCloseTime())
                .lunchStartTime(clinicAvailability.getLunchStartTime())
                .lunchEndTime(clinicAvailability.getLunchEndTime())
                .isWorkingDay(clinicAvailability.getIsWorkingDay())
                .build();
    }
}
