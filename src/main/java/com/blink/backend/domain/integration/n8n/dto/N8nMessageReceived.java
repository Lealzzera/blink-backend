package com.blink.backend.domain.integration.n8n.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class N8nMessageReceived {
    private String message;
    private String sender;
}
