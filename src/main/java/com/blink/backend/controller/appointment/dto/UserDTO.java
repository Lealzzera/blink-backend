package com.blink.backend.controller.appointment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {
    private Integer id;
    private String name;
}
