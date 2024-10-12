package com.gustavolyra.desafio_backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_wallets")
public class Wallet implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigDecimal balance;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer version;

    @OneToOne(mappedBy = "wallet")
    private User user;

    @PrePersist
    public void createdAt() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = Instant.now();
    }


    public Wallet(UUID id, BigDecimal balance, Integer version) {
        this.id = id;
        this.balance = balance;
        this.version = version;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id) && Objects.equals(balance, wallet.balance) && Objects.equals(createdAt, wallet.createdAt) && Objects.equals(updatedAt, wallet.updatedAt) && Objects.equals(version, wallet.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, createdAt, updatedAt, version);
    }
}
