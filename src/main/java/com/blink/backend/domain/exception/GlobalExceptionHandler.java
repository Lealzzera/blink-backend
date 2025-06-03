package com.blink.backend.domain.exception;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(
                        ErrorResponse.builder()
                                .code(exception.getCode())
                                .message(exception.getMessage())
                                .build());
    }

}
