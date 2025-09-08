package org.taller01.AccountMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.taller01.AccountMS.domain.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
  Optional<Account> findByAccountNumber(String accountNumber);

  boolean existsByAccountNumber(String accountNumber);

  List<Account> findByClientId(String clientId);
}
