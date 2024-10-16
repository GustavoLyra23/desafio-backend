package com.gustavolyra.desafio_backend.models.dto.user;

import com.gustavolyra.desafio_backend.models.dto.role.RoleDto;
import com.gustavolyra.desafio_backend.models.entities.User;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String fullName;
    private final String email;
    private final Set<RoleDto> roles = new HashSet<>();

    public UserResponseDto(Long id, String fullName, String email, Set<RoleDto> roles) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.roles.addAll(roles);
    }

    public UserResponseDto(User entity) {
        id = entity.getId();
        fullName = entity.getFullName();
        email = entity.getEmail();
        entity.getRoles().forEach(role -> roles.add(new RoleDto(role.getId(), role.getAuthority())));
    }

}
