package com.blink.backend.controller.message.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatHistoryDto {
    private String text;
    private Boolean fromMe;
    private LocalDateTime sentAt;
    private AckStatus ack;
}
