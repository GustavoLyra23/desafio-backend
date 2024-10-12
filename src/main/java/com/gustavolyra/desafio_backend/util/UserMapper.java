package com.gustavolyra.desafio_backend.util;

import com.gustavolyra.desafio_backend.models.dto.user.UserRequestDto;
import com.gustavolyra.desafio_backend.models.entities.User;

public class UserMapper {


    public static void map(UserRequestDto source, User destination) {
        destination.setCpf(source.cpf());
        destination.setEmail(source.email());
        destination.setFullName(source.fullName());
    }
}
