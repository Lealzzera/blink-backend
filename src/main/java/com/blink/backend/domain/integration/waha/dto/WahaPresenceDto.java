package com.blink.backend.domain.integration.waha.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WahaPresenceDto {
    private String chatId;
    private String session;
}
