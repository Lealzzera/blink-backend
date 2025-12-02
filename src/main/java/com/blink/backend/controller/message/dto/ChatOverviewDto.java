package com.blink.backend.controller.message.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatOverviewDto {
    private String phoneNumber;
    private String pictureUrl;
    private String patientName;
    private String whatsAppName;
    private String lastMessage;
    private LocalDateTime sentAt;
    private Boolean fromMe;
    private Boolean aiAnswer;
    private AckStatus messageStatus;
}
