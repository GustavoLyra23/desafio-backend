package com.gustavolyra.desafio_backend.models.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferDto(@Positive(message = "value must be positive") BigDecimal value,
                          @Positive(message = "payer id must be positive") Long payer,
                          @Positive(message = "payee id must be positive") Long payee) {
}
