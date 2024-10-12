package com.gustavolyra.desafio_backend.models.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserRequestDto(@NotBlank(message = "name cant be blank") @Size(max = 36) String fullName,
                             @Email(message = "invalid email") String email,
                             @Size(min = 3, max = 8, message = "password size must be between 3 or 8")
                             String password,
                             @CPF(message = "invalid cpf") String cpf) {
}
