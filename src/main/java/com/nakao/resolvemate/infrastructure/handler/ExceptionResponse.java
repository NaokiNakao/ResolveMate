package com.nakao.resolvemate.infrastructure.handler;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private final String title;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;
}
