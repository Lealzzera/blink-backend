package com.blink.backend.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .code("unauthorized")
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Formato de requisição inválido.";
        String field = "";
        String code = "invalid.format";

        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            field = cause.getPath().stream()
                         .map(com.fasterxml.jackson.databind.JsonMappingException.Reference::getFieldName)
                         .collect(Collectors.joining("."));
            message = String.format("Formato inválido para o campo '%s'. Valor recebido: '%s'", field, cause.getValue());
            code = "invalid.field.format";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(code)
                        .message(message)
                        .build());
    }
}
