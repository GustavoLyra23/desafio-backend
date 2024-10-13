package com.gustavolyra.desafio_backend.services;

import com.gustavolyra.desafio_backend.exceptions.InvalidCredentialsException;
import com.gustavolyra.desafio_backend.models.dto.user.UserLoginDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserRequestDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserResponseDto;
import com.gustavolyra.desafio_backend.models.entities.User;
import com.gustavolyra.desafio_backend.models.entities.Wallet;
import com.gustavolyra.desafio_backend.repositories.RoleRepository;
import com.gustavolyra.desafio_backend.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGeneratorService tokenGeneratorService;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenGeneratorService tokenGeneratorService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGeneratorService = tokenGeneratorService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public UserResponseDto register(@Valid UserRequestDto userRequestDto) {
        User user = new User();
        dtoToEntity(userRequestDto, user);
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        user.setWallet(wallet);
        user = userRepository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public String login(@Valid UserLoginDto userLoginDto) {
        var user = userRepository.findByEmail(userLoginDto.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(userLoginDto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return tokenGeneratorService.generateToken(user);
    }

    private void dtoToEntity(UserRequestDto userRequestDto, User user) {
        user.setFullName(userRequestDto.fullName());
        user.setCpf(userRequestDto.cpf());
        user.setEmail(userRequestDto.email());
        user.getRoles().add(roleRepository.getReferenceById(1L));
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
    }
}
