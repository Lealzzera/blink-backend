package com.blink.backend.controller.message.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatOverviewDto {
    private String phoneNumber;
    private String pictureUrl;
    private String text;
    private Boolean fromMe;
}
