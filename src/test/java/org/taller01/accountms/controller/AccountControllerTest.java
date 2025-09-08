package org.taller01.AccountMS.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.taller01.AccountMS.domain.Account;
import org.taller01.AccountMS.domain.AccountType;
import org.taller01.AccountMS.dto.request.BalanceOperationRequest;
import org.taller01.AccountMS.dto.request.CreateAccountRequest;
import org.taller01.AccountMS.service.AccountService;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;
    private CreateAccountRequest createRequest;
    private BalanceOperationRequest operationRequest;

    @BeforeEach
    void setUp() {
        testAccount = Account.builder()
                .id("1")
                .clientId("CLIENT123")
                .accountNumber("ACC1234567890")
                .currency("PEN")
                .balance(new BigDecimal("1000.00"))
                .active(true)
                .type(AccountType.SAVINGS)
                .build();

        createRequest = new CreateAccountRequest("CLIENT123", AccountType.SAVINGS, new BigDecimal("500.00"));
        operationRequest = new BalanceOperationRequest(new BigDecimal("200.00"));
    }

    @Test
    void list_ShouldReturnAllAccounts() throws Exception {
        // Given
        List<Account> accounts = Arrays.asList(testAccount);
        when(accountService.listAll()).thenReturn(accounts);

        // When & Then
        mockMvc.perform(get("/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].accountNumber").value("ACC1234567890"))
                .andExpect(jsonPath("$[0].balance").value(1000.00));
    }

    @Test
    void getById_WhenAccountExists_ShouldReturnAccount() throws Exception {
        // Given
        when(accountService.getById("1")).thenReturn(testAccount);

        // When & Then
        mockMvc.perform(get("/cuentas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.accountNumber").value("ACC1234567890"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void createAccount_WithValidRequest_ShouldCreateAccount() throws Exception {
        // Given
        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(testAccount);

        // When & Then
        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.accountNumber").value("ACC1234567890"));
    }

    @Test
    void createAccount_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        CreateAccountRequest invalidRequest = new CreateAccountRequest("", AccountType.SAVINGS, new BigDecimal("-100"));

        // When & Then
        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_WithValidAmount_ShouldReturnUpdatedAccount() throws Exception {
        // Given
        Account updatedAccount = Account.builder()
                .id("1")
                .clientId("CLIENT123")
                .accountNumber("ACC1234567890")
                .currency("PEN")
                .balance(new BigDecimal("1200.00"))
                .active(true)
                .type(AccountType.SAVINGS)
                .build();

        when(accountService.deposit(anyString(), any(BigDecimal.class))).thenReturn(updatedAccount);

        // When & Then
        mockMvc.perform(put("/cuentas/1/depositar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1200.00));
    }

    @Test
    void withdraw_WithValidAmount_ShouldReturnUpdatedAccount() throws Exception {
        // Given
        Account updatedAccount = Account.builder()
                .id("1")
                .clientId("CLIENT123")
                .accountNumber("ACC1234567890")
                .currency("PEN")
                .balance(new BigDecimal("800.00"))
                .active(true)
                .type(AccountType.SAVINGS)
                .build();

        when(accountService.withdraw(anyString(), any(BigDecimal.class))).thenReturn(updatedAccount);

        // When & Then
        mockMvc.perform(put("/cuentas/1/retirar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800.00));
    }

    @Test
    void deleteAccount_ShouldReturnNoContent() throws Exception {
        // When & Then
        mockMvc.perform(delete("/cuentas/1"))
                .andExpect(status().isNoContent());
    }
}
