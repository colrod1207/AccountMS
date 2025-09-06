package org.taller01.accountms.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taller01.accountms.domain.Account;
import org.taller01.accountms.exception.ResourceNotFoundException;
import org.taller01.accountms.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

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
    return accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
  }

}

