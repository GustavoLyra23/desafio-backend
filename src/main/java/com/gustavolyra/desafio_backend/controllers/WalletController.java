package com.gustavolyra.desafio_backend.controllers;

import com.gustavolyra.desafio_backend.models.dto.wallet.TransferDto;
import com.gustavolyra.desafio_backend.models.dto.wallet.WalletDto;
import com.gustavolyra.desafio_backend.services.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wallet")
public class WalletController {

    private final WalletService walletService;


    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferDto transferDto) {
        walletService.transfer(transferDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/data")
    @PreAuthorize("hasAnyRole('USER', 'SHOPKEEP')")
    public ResponseEntity<WalletDto> getWalletData() {
        var wallet = walletService.findWalletData();
        return ResponseEntity.ok(wallet);
    }



}
