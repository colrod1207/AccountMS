package org.taller01.accountms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.taller01.accountms.domain.Account;
import org.taller01.accountms.domain.AccountType;
import org.taller01.accountms.dto.request.CreateAccountRequest;
import org.taller01.accountms.dto.request.UpdateAccountRequest;
import org.taller01.accountms.exception.ResourceNotFoundException;
import org.taller01.accountms.repository.AccountRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import static org.springframework.http.HttpStatus.*;

@Service
public class AccountService {


  private final AccountRepository repo;
  private final WebClient webClient;

  public AccountService(AccountRepository repo,
                        WebClient.Builder builder,
                        @Value("${clientms.base-url}") String baseUrl) {
    this.repo = repo;
    this.webClient = builder.baseUrl(baseUrl).build();
  }

  public Flux<Account> listAll() { return repo.findAll(); }

  public Flux<Account> listByClientId(String clientId) {
    return validarCliente(clientId).thenMany(repo.findByClientId(clientId));
  }

  public Mono<Account> getById(String id) {
    return repo.findById(id)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada")));
  }

  public Mono<Account> create(CreateAccountRequest req) {
    return validarCliente(req.clientId())
            .then(generarNumeroCuentaUnico(req.accountType()))
            .flatMap(num -> {
              var acc = Account.builder()
                      .clientId(req.clientId())
                      .accountNumber(num)
                      .balance(req.initialBalance() == null ? BigDecimal.ZERO : req.initialBalance())
                      .active(true)
                      .type(req.accountType())
                      .build();
              return repo.save(acc);
            });
  }

  public Mono<Account> updatePut(String id, UpdateAccountRequest req) {
    return getById(id).flatMap(acc -> {
      if (req.active() == null)
        return Mono.error(new ResponseStatusException(BAD_REQUEST, "El campo 'active' es obligatorio"));
      if (req.active().equals(acc.getActive()))
        return Mono.error(new ResponseStatusException(CONFLICT,
                Boolean.TRUE.equals(acc.getActive()) ? "La cuenta ya está activada" : "La cuenta ya está desactivada"));
      acc.setActive(req.active());
      return repo.save(acc);
    });
  }

  public Mono<Account> updatePatch(String id, UpdateAccountRequest req) {
    return getById(id).flatMap(acc -> {
      if (req.active() == null)
        return Mono.error(new ResponseStatusException(BAD_REQUEST, "No se envió ningún cambio"));
      if (req.active().equals(acc.getActive()))
        return Mono.error(new ResponseStatusException(CONFLICT,
                Boolean.TRUE.equals(acc.getActive()) ? "La cuenta ya está activada" : "La cuenta ya está desactivada"));
      acc.setActive(req.active());
      return repo.save(acc);
    });
  }

  public Mono<Void> delete(String id) {
    return getById(id).flatMap(acc -> {
      if (acc.getBalance() != null && acc.getBalance().compareTo(BigDecimal.ZERO) != 0)
        return Mono.error(new ResponseStatusException(CONFLICT,
                "No se puede eliminar la cuenta: el saldo debe ser 0.00"));
      return repo.delete(acc);
    });
  }

  private Mono<Void> validarCliente(String clientId) {
    return webClient.get().uri("/clientes/{id}", clientId)
            .exchangeToMono(resp -> {
              if (resp.statusCode().is2xxSuccessful()) return Mono.empty();
              if (resp.statusCode().value() == 404)
                return Mono.error(new ResponseStatusException(BAD_REQUEST, "El cliente no existe"));
              if (resp.statusCode().value() == 400)
                return Mono.error(new ResponseStatusException(BAD_REQUEST, "El identificador de cliente es inválido"));
              if (resp.statusCode().is5xxServerError())
                return Mono.error(new ResponseStatusException(SERVICE_UNAVAILABLE, "ClientMS no disponible"));
              return Mono.error(new ResponseStatusException(BAD_REQUEST, "No se pudo validar el cliente"));
            });
  }

  private Mono<String> generarNumeroCuentaUnico(AccountType t) {
    final String pref = (t == AccountType.SAVINGS) ? "SV" : "CH";
    return Mono.defer(() -> {
              String cand = String.format("%s-%06d", pref, ThreadLocalRandom.current().nextInt(1_000_000));
              return repo.existsByAccountNumber(cand)
                      .flatMap(exists -> exists ? Mono.error(new IllegalStateException("dup")) : Mono.just(cand));
            })
            .retry(10)
            .onErrorMap(e -> new ResponseStatusException(CONFLICT, "No se pudo generar un número de cuenta único"));
  }

  // ============================================================
  // COMANDOS DE DOMINIO (usados por TransactionMS)
  // ============================================================

  public Mono<Account> deposit(String accountId, BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      return Mono.error(new ResponseStatusException(BAD_REQUEST, "El monto debe ser positivo"));
    }
    return getById(accountId).flatMap(acc -> {
      BigDecimal current = acc.getBalance() == null ? BigDecimal.ZERO : acc.getBalance();
      acc.setBalance(current.add(amount));
      return repo.save(acc);
    });
  }

  public Mono<Account> withdraw(String accountId, BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      return Mono.error(new ResponseStatusException(BAD_REQUEST, "El monto debe ser positivo"));
    }
    return getById(accountId).flatMap(acc -> {
      BigDecimal current = acc.getBalance() == null ? BigDecimal.ZERO : acc.getBalance();
      if (current.compareTo(amount) < 0) {
        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Saldo insuficiente"));
      }
      acc.setBalance(current.subtract(amount));
      return repo.save(acc);
    });
  }

}
