package com.blink.backend.controller.message.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatOverviewDto {
    private String phoneNumber;
    private String pictureUrl;
    private String patientName;
    private String lastMessage;
    private LocalDateTime sentAt;
    private Boolean fromMe;
    private Boolean aiAnswer;
}
