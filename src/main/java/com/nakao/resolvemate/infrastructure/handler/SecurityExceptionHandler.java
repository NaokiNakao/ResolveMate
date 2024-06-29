package com.nakao.resolvemate.infrastructure.handler;

import com.nakao.resolvemate.domain.util.LogService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class SecurityExceptionHandler {

    private final LogService<SecurityExceptionHandler> logService;

    @ExceptionHandler(value = {BadCredentialsException.class, ExpiredJwtException.class, SignatureException.class})
    public ResponseEntity<Object> handleSecurityException(Exception e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        String message = e.getMessage();
        String title = null;

        if (e instanceof BadCredentialsException) {
            title = "BAD_CREDENTIALS";
        }
        if (e instanceof ExpiredJwtException) {
            title = "EXPIRED_TOKEN";
        }
        if (e instanceof SignatureException) {
            title = "INVALID_TOKEN";
        }

        logService.warn(this, message);

        return new  ResponseEntity<>(ExceptionResponse.builder()
                .title(title)
                .message(message)
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build(),
                httpStatus);
    }

}
