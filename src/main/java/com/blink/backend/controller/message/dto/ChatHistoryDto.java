package com.blink.backend.controller.message.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatHistoryDto {
    private String messageText;
    private Boolean fromMe;
    private LocalDateTime sentAt;
    private AckStatus ack;
}
