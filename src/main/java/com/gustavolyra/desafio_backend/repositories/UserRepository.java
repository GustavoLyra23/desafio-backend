package com.gustavolyra.desafio_backend.repositories;

import com.gustavolyra.desafio_backend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.wallet WHERE u.email = :email")
    Optional<User> findUserWithWallet(String email);

}
