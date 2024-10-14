package com.gustavolyra.desafio_backend.controllers;

import com.gustavolyra.desafio_backend.models.dto.wallet.TransferDto;
import com.gustavolyra.desafio_backend.models.dto.wallet.WalletDto;
import com.gustavolyra.desafio_backend.services.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    @WithMockUser(roles = "USER")
    void testTransfer_WithUserRole_ShouldAllowAccess() throws Exception {
        // Arrange
        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(100), 2L);

        // Act & Assert
        mockMvc.perform(post("/v1/wallet/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\": 100, \"payee\": 2}"))
                .andExpect(status().isOk());

        Mockito.verify(walletService, Mockito.times(1)).transfer(Mockito.any(TransferDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetWalletData_WithUserRole_ShouldAllowAccess() throws Exception {
        // Arrange
        WalletDto walletDto = new WalletDto(1L, BigDecimal.valueOf(500), 1, null, null);
        Mockito.when(walletService.findWalletData()).thenReturn(walletDto);

        // Act & Assert
        mockMvc.perform(get("/v1/wallet/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletDto.getId()))
                .andExpect(jsonPath("$.balance").value(walletDto.getBalance().intValue())); // Expecting int for simplicity

        Mockito.verify(walletService, Mockito.times(1)).findWalletData();
    }

    @Test
    @WithMockUser(roles = "ANOTHER_ROLE")
    void testTransfer_WithIncorrectRole_ShouldDenyAccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/v1/wallet/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\": 100, \"payee\": 2}"))
                .andExpect(status().isForbidden());

        Mockito.verify(walletService, Mockito.times(0)).transfer(Mockito.any(TransferDto.class));
    }

    @Test
    @WithMockUser(roles = "ANOTHER_ROLE")
    void testGetWalletData_WithIncorrectRole_ShouldDenyAccess() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/wallet/data"))
                .andExpect(status().isForbidden());

        Mockito.verify(walletService, Mockito.times(0)).findWalletData();
    }

    @Test
    void testTransfer_WithoutAuthentication_ShouldDenyAccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/v1/wallet/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\": 100, \"payee\": 2}"))
                .andExpect(status().isForbidden());

        Mockito.verify(walletService, Mockito.times(0)).transfer(Mockito.any(TransferDto.class));
    }

    @Test
    void testGetWalletData_WithoutAuthentication_ShouldDenyAccess() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/wallet/data"))
                .andExpect(status().isForbidden());

        Mockito.verify(walletService, Mockito.times(0)).findWalletData();
    }
}
