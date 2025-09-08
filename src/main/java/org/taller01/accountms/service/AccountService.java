package org.taller01.AccountMS.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taller01.AccountMS.domain.Account;
import org.taller01.AccountMS.dto.request.CreateAccountRequest;
import org.taller01.AccountMS.exception.AccountInactiveException;
import org.taller01.AccountMS.exception.InsufficientFundsException;
import org.taller01.AccountMS.exception.ResourceNotFoundException;
import org.taller01.AccountMS.repository.AccountRepository;

@Service
@AllArgsConstructor
public class AccountService {

  private AccountRepository accountRepository;

  @Transactional(readOnly = true)
  public List<Account> listAll() {
    return accountRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Account getById(String accountId) {
    return accountRepository.findById(accountId).orElseThrow(
        () -> new ResourceNotFoundException("Account not found with id: " + accountId));
  }

  @Transactional
  public Account createAccount(CreateAccountRequest request) {
    // Validar que el balance inicial no sea negativo
    if (request.initialBalance().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Initial balance cannot be negative");
    }

    // Generar número de cuenta único
    String accountNumber;
    do {
      accountNumber = generateAccountNumber();
    } while (accountRepository.existsByAccountNumber(accountNumber));

    Account account =
        Account.builder().clientId(request.clientId()).accountNumber(accountNumber).currency("PEN") // Moneda
                                                                                                    // por
                                                                                                    // defecto
            .balance(request.initialBalance()).active(true).type(request.type()).build();

    return accountRepository.save(account);
  }

  @Transactional
  public Account deposit(String accountId, BigDecimal amount) {
    // Validar que el monto sea positivo
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Deposit amount must be positive");
    }

    Account account = getById(accountId);

    if (!account.getActive()) {
      throw new AccountInactiveException(
          "Cannot perform operations on inactive account: " + accountId);
    }

    account.setBalance(account.getBalance().add(amount));
    return accountRepository.save(account);
  }

  @Transactional
  public Account withdraw(String accountId, BigDecimal amount) {
    // Validar que el monto sea positivo
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Withdrawal amount must be positive");
    }

    Account account = getById(accountId);

    if (!account.getActive()) {
      throw new AccountInactiveException(
          "Cannot perform operations on inactive account: " + accountId);
    }

    if (account.getBalance().compareTo(amount) < 0) {
      throw new InsufficientFundsException(
          "Insufficient funds. Available: " + account.getBalance() + ", Requested: " + amount);
    }

    account.setBalance(account.getBalance().subtract(amount));
    return accountRepository.save(account);
  }

  @Transactional
  public void deleteAccount(String accountId) {
    Account account = getById(accountId);

    // Solo permitir eliminación si el balance es cero
    if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
      throw new IllegalStateException(
          "Cannot delete account with non-zero balance: " + account.getBalance());
    }

    accountRepository.delete(account);
  }

  private String generateAccountNumber() {
    return "ACC" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
  }
}
