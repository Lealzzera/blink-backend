package com.blink.backend.domain.integration.waha.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WahaWebhooks {
    private String url;
    private List<WahaWebhookEventTypes> events;
}
