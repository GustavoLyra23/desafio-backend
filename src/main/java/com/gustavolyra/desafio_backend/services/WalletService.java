package com.gustavolyra.desafio_backend.services;

import com.gustavolyra.desafio_backend.exceptions.ExternalServiceException;
import com.gustavolyra.desafio_backend.exceptions.ForbiddenException;
import com.gustavolyra.desafio_backend.models.dto.wallet.TransferDto;
import com.gustavolyra.desafio_backend.models.dto.wallet.WalletDto;
import com.gustavolyra.desafio_backend.models.entities.User;
import com.gustavolyra.desafio_backend.repositories.UserRepository;
import com.gustavolyra.desafio_backend.util.AuthUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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
        verifyPayeeIdentity(user.getId(), transferDto.payee());
        checkSufficientFunds(user, transferDto.value());
        var payee = userRepository.findById(transferDto.payee()).orElseThrow(() -> new UsernameNotFoundException("Payee not found"));
        authorizeTransaction();
        updateBalances(user, payee, transferDto.value());
        userRepository.saveAll(Set.of(user, payee));
        notifyPayee(payee.getEmail());
    }

    private void checkSufficientFunds(User user, BigDecimal amount) {
        if (user.getWallet().getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    private void notifyPayee(String payeeEmail) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String jsonBody = "{\"email\": \"" + payeeEmail + "\"}";
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ExternalServiceException("Could not notify payee");
            }
        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException("Could not notify payee");
        }
    }

    private void authorizeTransaction() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
            if (!verifyResponse(response)) {
                throw new ForbiddenException("Unauthorized");
            }
        } catch (HttpClientErrorException e) {
            throw new ExternalServiceException("Could not authorize transaction");
        }
    }

    private boolean verifyResponse(ResponseEntity<Map> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            return Boolean.TRUE.equals(data.get("authorization"));
        }
        return false;
    }

    private void updateBalances(User payer, User payee, BigDecimal amount) {
        var payerBalance = payer.getWallet().getBalance();
        payer.getWallet().setBalance(payerBalance.subtract(amount));
        payee.getWallet().setBalance(payee.getWallet().getBalance().add(amount));
    }

    private void verifyPayeeIdentity(Long payerId, Long payeeId) {
        if (payerId.equals(payeeId)) {
            throw new IllegalArgumentException("Payer and payee must be different");
        }
    }

    @Transactional(readOnly = true)
    public WalletDto findWalletData() {
        var user = AuthUtil.getAuthenticatedUser();
        return new WalletDto(user.getWallet());
    }
}