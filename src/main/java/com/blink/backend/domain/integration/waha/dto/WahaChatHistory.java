package com.blink.backend.domain.integration.waha.dto;

import com.blink.backend.controller.message.dto.AckStatus;
import com.blink.backend.controller.message.dto.ChatHistoryDto;
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
public class WahaChatHistory {
    private Long timestamp;
    private Boolean fromMe;
    private String body;
    private String ackName;

    public ChatHistoryDto toChatHistoryDto() {

        return ChatHistoryDto.builder()
                .messageText(body)
                .fromMe(fromMe)
                .sentAt(Instant.ofEpochSecond(timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .ack(AckStatus.valueOf(ackName))
                .build();
    }
}
