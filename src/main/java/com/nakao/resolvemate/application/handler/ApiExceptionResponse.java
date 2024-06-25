package com.nakao.resolvemate.application.handler;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
public class ApiExceptionResponse {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public ApiExceptionResponse(RuntimeException e, HttpStatus httpStatus) {
        this.message = e.getMessage();
        this.throwable = e;
        this.httpStatus = httpStatus;
        this.timestamp = ZonedDateTime.now(ZoneId.of("Z"));
    }
}
