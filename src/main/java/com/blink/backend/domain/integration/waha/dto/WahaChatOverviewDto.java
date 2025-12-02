package com.blink.backend.domain.integration.waha.dto;

import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class WahaChatOverviewDto {
    private String id;
    private String picture;
    private String name;
    private OverviewLastMessageDto lastMessage;


    public ChatOverviewDto toChatOverviewDto(Boolean aiAnswer, String patientName) {
        if (!id.contains("@c.us")) {
            return null;
        }

        return ChatOverviewDto.builder()
                .phoneNumber(id.replace("@c.us", ""))
                .pictureUrl(picture)
                .lastMessage(lastMessage.getMessage())
                .sentAt(Instant.ofEpochSecond(lastMessage.getTimestamp())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .fromMe(lastMessage.getFromMe())
                .aiAnswer(aiAnswer)
                .patientName(patientName)
                .whatsAppName(name)
                .messageStatus(lastMessage.getAckName())
                .build();
    }
}
