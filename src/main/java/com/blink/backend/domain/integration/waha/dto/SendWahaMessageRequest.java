package com.blink.backend.domain.integration.waha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendWahaMessageRequest {
    private String session;
    @JsonProperty("chatId")
    private String phoneNumber;
    private String text;
}
