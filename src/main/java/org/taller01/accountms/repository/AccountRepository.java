package org.taller01.accountms.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.taller01.accountms.domain.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
  Mono<Boolean> existsByAccountNumber(String accountNumber);

  Flux<Account> findByClientId(String clientId);
}
