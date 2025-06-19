package com.blink.backend.domain.integration.waha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WahaMeConfig {
    private String id;

    public String getPhoneNumber() {
        return id.replace("@c.us", "");
    }
}
