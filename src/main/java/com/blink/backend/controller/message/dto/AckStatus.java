package com.blink.backend.controller.message.dto;

import lombok.Getter;

@Getter
public enum AckStatus {
    ERROR("ERROR"),
    PENDING("PENDING"),
    SERVER("SENT"),
    DEVICE("RECEIVED"),
    READ("READ"),
    PLAYED("PLAYED"),
    UNKNOWN("UNKNOWN");

    /*ERROR,
    PENDING,
    SERVER,
    DEVICE,
    READ,
    PLAYED,
    UNKNOWN;*/

    private final String value;

    AckStatus(String value) {
        this.value = value;
    }
}
