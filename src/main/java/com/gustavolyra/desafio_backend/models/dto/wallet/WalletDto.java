package com.gustavolyra.desafio_backend.models.dto.wallet;

import com.gustavolyra.desafio_backend.models.entities.Wallet;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class WalletDto {

    private final Long id;
    private final BigDecimal balance;
    private final Integer version;
    private final Instant createdAt;
    private final Instant updatedAt;

    public WalletDto(Long id, BigDecimal balance, Integer version, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.balance = balance;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public WalletDto(Wallet wallet) {
        this.id = wallet.getId();
        this.balance = wallet.getBalance();
        this.version = wallet.getVersion();
        this.createdAt = wallet.getCreatedAt();
        this.updatedAt = wallet.getUpdatedAt();
    }


}
