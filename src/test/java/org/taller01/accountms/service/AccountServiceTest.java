package org.taller01.AccountMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.taller01.AccountMS.domain.Account;
import org.taller01.AccountMS.domain.AccountType;
import org.taller01.AccountMS.dto.request.CreateAccountRequest;
import org.taller01.AccountMS.exception.AccountInactiveException;
import org.taller01.AccountMS.exception.InsufficientFundsException;
import org.taller01.AccountMS.exception.ResourceNotFoundException;
import org.taller01.AccountMS.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;
    private CreateAccountRequest createRequest;

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
    }

    @Test
    void listAll_ShouldReturnAllAccounts() {
        // Given
        List<Account> accounts = Arrays.asList(testAccount);
        when(accountRepository.findAll()).thenReturn(accounts);

        // When
        List<Account> result = accountService.listAll();

        // Then
        assertEquals(1, result.size());
        assertEquals(testAccount, result.get(0));
        verify(accountRepository).findAll();
    }

    @Test
    void getById_WhenAccountExists_ShouldReturnAccount() {
        // Given
        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));

        // When
        Account result = accountService.getById("1");

        // Then
        assertEquals(testAccount, result);
        verify(accountRepository).findById("1");
    }

    @Test
    void getById_WhenAccountNotExists_ShouldThrowException() {
        // Given
        when(accountRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> accountService.getById("999"));
        verify(accountRepository).findById("999");
    }

    @Test
    void createAccount_WithValidRequest_ShouldCreateAccount() {
        // Given
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.createAccount(createRequest);

        // Then
        assertNotNull(result);
        verify(accountRepository).existsByAccountNumber(anyString());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_WithNegativeBalance_ShouldThrowException() {
        // Given
        CreateAccountRequest invalidRequest = new CreateAccountRequest("CLIENT123", AccountType.SAVINGS, new BigDecimal("-100.00"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(invalidRequest));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deposit_WithValidAmount_ShouldIncreaseBalance() {
        // Given
        BigDecimal depositAmount = new BigDecimal("200.00");
        BigDecimal expectedBalance = new BigDecimal("1200.00");
        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.deposit("1", depositAmount);

        // Then
        assertEquals(expectedBalance, result.getBalance());
        verify(accountRepository).findById("1");
        verify(accountRepository).save(testAccount);
    }

    @Test
    void deposit_WithInactiveAccount_ShouldThrowException() {
        // Given
        testAccount.setActive(false);
        BigDecimal depositAmount = new BigDecimal("200.00");
        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));

        // When & Then
        assertThrows(AccountInactiveException.class, () -> accountService.deposit("1", depositAmount));
        verify(accountRepository).findById("1");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void withdraw_WithSufficientFunds_ShouldDecreaseBalance() {
        // Given
        BigDecimal withdrawAmount = new BigDecimal("300.00");
        BigDecimal expectedBalance = new BigDecimal("700.00");
        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.withdraw("1", withdrawAmount);

        // Then
        assertEquals(expectedBalance, result.getBalance());
        verify(accountRepository).findById("1");
        verify(accountRepository).save(testAccount);
    }

    @Test
    void withdraw_WithInsufficientFunds_ShouldThrowException() {
        // Given
        BigDecimal withdrawAmount = new BigDecimal("1500.00");
        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));

        // When & Then
        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw("1", withdrawAmount));
        verify(accountRepository).findById("1");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deleteAccount_WithZeroBalance_ShouldDeleteAccount() {
        // Given
        testAccount.setBalance(BigDecimal.ZERO);
        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));

        // When
        accountService.deleteAccount("1");

        // Then
        verify(accountRepository).findById("1");
        verify(accountRepository).delete(testAccount);
    }

    @Test
    void deleteAccount_WithNonZeroBalance_ShouldThrowException() {
        // Given
        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));

        // When & Then
        assertThrows(IllegalStateException.class, () -> accountService.deleteAccount("1"));
        verify(accountRepository).findById("1");
        verify(accountRepository, never()).delete(any());
    }
}
