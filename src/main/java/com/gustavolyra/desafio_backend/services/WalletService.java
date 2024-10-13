package com.gustavolyra.desafio_backend.services;

import com.gustavolyra.desafio_backend.repositories.UserRepository;
import com.gustavolyra.desafio_backend.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WalletService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public WalletService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    public void transfer() {
        AuthUtil.getAuthenticatedUser();
    }


}
