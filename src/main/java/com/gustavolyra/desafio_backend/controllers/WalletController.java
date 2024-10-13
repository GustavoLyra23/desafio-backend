package com.gustavolyra.desafio_backend.controllers;

import com.gustavolyra.desafio_backend.services.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/wallet")
public class WalletController {

    private final WalletService walletService;


    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> transfer() {
        walletService.transfer();
        return ResponseEntity.ok().build();
    }

}
