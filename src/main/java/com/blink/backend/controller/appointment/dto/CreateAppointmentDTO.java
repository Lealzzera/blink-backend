package com.blink.backend.controller.appointment.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateAppointmentDTO {

    private String patientNumber;
    @JsonProperty("clinic")
    private Integer clinicId;
    @JsonProperty("service_type")
    private Integer serviceTypeId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime scheduledTime;
    private String notes;

    public LocalDateTime getScheduledTimeEnd(Integer duration){
        return this.getScheduledTime()
                .plusMinutes(duration)
                .truncatedTo(ChronoUnit.MINUTES);
    }
}
