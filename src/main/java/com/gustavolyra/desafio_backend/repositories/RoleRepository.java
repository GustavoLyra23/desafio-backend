package com.gustavolyra.desafio_backend.repositories;

import com.gustavolyra.desafio_backend.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
