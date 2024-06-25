package com.nakao.resolvemate.application.handler;

import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.UnauthorizedAccessException;
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

    @ExceptionHandler(value = {UnauthorizedAccessException.class})
    public ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiExceptionResponse response = new ApiExceptionResponse(e, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

}
