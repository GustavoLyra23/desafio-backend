package com.gustavolyra.desafio_backend.controllers;

import com.gustavolyra.desafio_backend.models.dto.user.UserLoginDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserRequestDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserResponseDto;
import com.gustavolyra.desafio_backend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        var user = authService.register(userRequestDto);
        URI uri = URI.create("/v1/auth/register/" + user.getId());
        return ResponseEntity.created(uri).body(user);

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        var token = authService.login(userLoginDto);
        return ResponseEntity.ok(Map.of("token", token));
    }

}
