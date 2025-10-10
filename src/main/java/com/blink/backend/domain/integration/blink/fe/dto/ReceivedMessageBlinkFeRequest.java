package com.blink.backend.domain.integration.blink.fe.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceivedMessageBlinkFeRequest {
    private String phoneNumber;
    private String message;
    private Integer clinicId;
    private Boolean fromMe;
}
