package com.gustavolyra.desafio_backend.repositories;

import com.gustavolyra.desafio_backend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
