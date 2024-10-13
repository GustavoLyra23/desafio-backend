package com.gustavolyra.desafio_backend.controllers.handlers;

import com.gustavolyra.desafio_backend.exceptions.ExternalServiceException;
import com.gustavolyra.desafio_backend.exceptions.ForbiddenException;
import com.gustavolyra.desafio_backend.exceptions.InvalidCredentialsException;
import com.gustavolyra.desafio_backend.exceptions.ResourceNotFoundException;
import com.gustavolyra.desafio_backend.models.dto.error.FieldError;
import com.gustavolyra.desafio_backend.models.dto.error.StandardError;
import com.gustavolyra.desafio_backend.models.dto.error.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<StandardError> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        var error = new StandardError(Instant.now(), status.value(), "Forbidden", request.getRequestURI().toString());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError validationError = new ValidationError(Instant.now(), status.value(), "Validation error", request.getRequestURI());
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationError.addFieldError(new FieldError(error.getField(), error.getDefaultMessage())));
        return ResponseEntity.status(status).body(validationError);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<StandardError> handleInvalidCredentialsException(InvalidCredentialsException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        var error = new StandardError(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI().toString());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<StandardError> handleExternalServiceException(ExternalServiceException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        var error = new StandardError(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI().toString());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StandardError> handleUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardError> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError error = new StandardError(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }


}
