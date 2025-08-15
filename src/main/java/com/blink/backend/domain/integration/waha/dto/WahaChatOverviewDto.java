package com.blink.backend.domain.integration.waha.dto;

import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class WahaChatOverviewDto {
    private String id;
    private String picture;
    private OverviewLastMessageDto lastMessage;

    public ChatOverviewDto toChatOverviewDto(Boolean aiAnswer, String patientName) {
        if (id.contains("@g.us") || id.contains("status@")) {
            return null;
        }
        return ChatOverviewDto.builder()
                .phoneNumber(id.replace("@c.us", ""))
                .pictureUrl(picture)
                .lastMessage(lastMessage.getMessage())
                .sentAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(lastMessage.getTimestamp()), TimeZone.getDefault().toZoneId()))
                .fromMe(lastMessage.getFromMe())
                .aiAnswer(aiAnswer)
                .patientName(patientName)
                .build();
    }
}
