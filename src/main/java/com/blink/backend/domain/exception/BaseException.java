package com.blink.backend.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends Exception {
    private final String code;
    private final String message;

    public abstract HttpStatus getStatusCode();

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
