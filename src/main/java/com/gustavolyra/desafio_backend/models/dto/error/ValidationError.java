package com.gustavolyra.desafio_backend.models.dto.error;

import lombok.Getter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ValidationError extends StandardError {

    private final Set<FieldError> fieldErrors = new HashSet<>();

    public ValidationError(Instant instant, Integer status, String error, String path) {
        super(instant, status, error, path);
    }


    public void addFieldError(FieldError fieldError) {
        fieldErrors.add(fieldError);
    }

}
