package com.blink.backend.domain.integration.waha.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class WahaWebhooks {
    private String url;
    private List<WahaWebhookEventTypes> events;
    private List<WahaCustomHeaders> customHeaders;
}
