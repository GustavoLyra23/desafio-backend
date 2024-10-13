package com.gustavolyra.desafio_backend.services;

import com.gustavolyra.desafio_backend.exceptions.ForbiddenException;
import com.gustavolyra.desafio_backend.exceptions.ResourceNotFound;
import com.gustavolyra.desafio_backend.models.dto.TransferDto;
import com.gustavolyra.desafio_backend.repositories.UserRepository;
import com.gustavolyra.desafio_backend.util.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Service
public class WalletService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public WalletService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @Transactional
    public void transfer(TransferDto transferDto) {
        var user = AuthUtil.getAuthenticatedUser();
        if (user.getWallet().getBalance().compareTo(transferDto.value()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        var payer = userRepository.findById(transferDto.payer())
                .orElseThrow(() -> new ResourceNotFound("User not found"));
        var payee = userRepository.findById(transferDto.payee())
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        ResponseEntity<Map> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        if (!verifyResponse(response)) {
            throw new ForbiddenException("Unauthorized");
        }

        var payerBalance = payer.getWallet().getBalance();
        payer.getWallet().setBalance(payerBalance.subtract(transferDto.value()));
        payee.getWallet().setBalance(payee.getWallet().getBalance().add(transferDto.value()));
        userRepository.saveAll(Set.of(payer, payee));
    }

    private boolean verifyResponse(ResponseEntity<Map> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            return Boolean.TRUE.equals(data.get("authorization"));
        }
        return false;
    }


}
