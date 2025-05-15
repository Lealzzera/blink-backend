package com.blink.backend.domain.model.appointment;


import com.blink.backend.persistence.entity.auth.Users;
import com.blink.backend.persistence.entity.clinic.Clinic;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ClinicAvailability {

            private Long id;

            private Clinic clinic;

            private WeekDay weekDay;

        private LocalTime openTime;

        private LocalTime closeTime;

        private LocalTime lunchStartTime;

        private LocalTime lunchEndTime;

            private Users updatedByUserId;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private Boolean isWorkingDay = true;
}
