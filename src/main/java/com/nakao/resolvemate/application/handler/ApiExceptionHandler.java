package com.nakao.resolvemate.application.handler;

import com.nakao.resolvemate.domain.exception.FileHandlingException;
import com.nakao.resolvemate.domain.exception.FileSizeLimitExceededException;
import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.ForbiddenAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiExceptionResponse response = new ApiExceptionResponse(e, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(value = {ForbiddenAccessException.class})
    public ResponseEntity<Object> handleForbiddenAccessException(ForbiddenAccessException e) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ApiExceptionResponse response = new ApiExceptionResponse(e, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(value = {FileHandlingException.class})
    public ResponseEntity<Object> handleFileUploadException(FileHandlingException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiExceptionResponse response = new ApiExceptionResponse(e, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(value = {FileSizeLimitExceededException.class})
    public ResponseEntity<Object> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        HttpStatus httpStatus = HttpStatus.PAYLOAD_TOO_LARGE;
        ApiExceptionResponse response = new ApiExceptionResponse(e, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

}
