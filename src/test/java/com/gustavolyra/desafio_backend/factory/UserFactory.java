package com.gustavolyra.desafio_backend.factory;

import com.gustavolyra.desafio_backend.models.dto.role.RoleDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserRequestDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserResponseDto;

import java.util.Set;

public class UserFactory {

    public static UserRequestDto createUserRequestDto() {
        return new UserRequestDto("test name", "test@gmail.com",
                "12345", "77826306060");
    }

    public static UserResponseDto createUserResponseDto() {
        return new UserResponseDto(1L, "test name", "test@gmail.com",
                Set.of(new RoleDto(1L, "ROLE_USER")));
    }
}