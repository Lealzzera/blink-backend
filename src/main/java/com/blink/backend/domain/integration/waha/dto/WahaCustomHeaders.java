package com.blink.backend.domain.integration.waha.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WahaCustomHeaders {
    private String name;
    private String value;
}
