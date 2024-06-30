package com.nakao.resolvemate.infrastructure.handler;

import com.nakao.resolvemate.domain.exception.FileHandlingErrorException;
import com.nakao.resolvemate.domain.exception.FileSizeLimitExceededException;
import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.ForbiddenAccessException;
import com.nakao.resolvemate.domain.util.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final LogService<ApiExceptionHandler> logService;

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ExceptionResponse response = ExceptionResponse.builder()
                .title("RESOURCE_NOT_FOUND")
                .message(e.getMessage())
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();

        logService.warn(this, message);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(value = {ForbiddenAccessException.class})
    public ResponseEntity<Object> handleForbiddenAccessException(ForbiddenAccessException e) {
        String message = e.getMessage();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        ExceptionResponse response = ExceptionResponse.builder()
                .title("FORBIDDEN_ACCESS")
                .message(e.getMessage())
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();

        logService.warn(this, message);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(value = {FileHandlingErrorException.class})
    public ResponseEntity<Object> handleFileUploadException(FileHandlingErrorException e) {
        String message = e.getMessage();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ExceptionResponse response = ExceptionResponse.builder()
                .title("FILE_HANDLING_ERROR")
                .message(e.getMessage())
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();

        logService.error(this, message);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(value = {FileSizeLimitExceededException.class})
    public ResponseEntity<Object> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        String message = e.getMessage();
        HttpStatus httpStatus = HttpStatus.PAYLOAD_TOO_LARGE;

        ExceptionResponse response = ExceptionResponse.builder()
                .title("FILE_SIZE_LIMIT_EXCEEDED")
                .message(e.getMessage())
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();

        logService.warn(this, message);
        return new ResponseEntity<>(response, httpStatus);
    }

}
