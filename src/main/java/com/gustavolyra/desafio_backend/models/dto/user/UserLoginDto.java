package com.gustavolyra.desafio_backend.models.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserLoginDto(@Email(message = "invalid email") String email,
                           @Size(min = 3, max = 8, message = "passowrd size must be between 3 and 8")
                           String password) {
}
