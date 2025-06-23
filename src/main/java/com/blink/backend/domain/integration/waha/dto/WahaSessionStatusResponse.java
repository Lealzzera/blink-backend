package com.blink.backend.domain.integration.waha.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class WahaSessionStatusResponse {
    private String name;
    private WahaSessionStatus status;
    private WahaSessionConfig config;
    private WahaMeConfig me;
}
