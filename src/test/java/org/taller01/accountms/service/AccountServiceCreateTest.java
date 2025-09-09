package org.taller01.accountms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.server.ResponseStatusException;
import org.taller01.accountms.domain.Account;
import org.taller01.accountms.domain.AccountType;
import org.taller01.accountms.dto.request.CreateAccountRequest;
import org.taller01.accountms.repository.AccountRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceCreateTest {

    private AccountRepository repo;

    private WebClient.Builder builderOk() {
        // Simula ClientMS OK (200)
        ExchangeFunction ok = req -> Mono.just(ClientResponse.create(HttpStatus.OK).build());
        return WebClient.builder().exchangeFunction(ok);
    }

    private WebClient.Builder builder404() {
        ExchangeFunction nf = req -> Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND).build());
        return WebClient.builder().exchangeFunction(nf);
    }

    private WebClient.Builder builder500() {
        ExchangeFunction err = req -> Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).build());
        return WebClient.builder().exchangeFunction(err);
    }

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(AccountRepository.class);
    }

    @Test
    void create_ok_validatesClient_andSaves() {
        var service = new AccountService(repo, builderOk(), "http://fake-clientms");
        var req = new CreateAccountRequest("C1", new BigDecimal("100.00"), AccountType.SAVINGS);

        when(repo.existsByAccountNumber(any())).thenReturn(Mono.just(false));
        when(repo.save(any(Account.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.create(req))
                .expectNextMatches(acc ->
                        acc.getClientId().equals("C1")
                                && acc.getType() == AccountType.SAVINGS
                                && acc.getBalance().compareTo(new BigDecimal("100.00")) == 0
                                && acc.getAccountNumber() != null)
                .verifyComplete();

        verify(repo).save(any(Account.class));
    }

    @Test
    void create_clientNotFound_mapsTo400() {
        var service = new AccountService(repo, builder404(), "http://fake-clientms");
        var req = new CreateAccountRequest("X", BigDecimal.ZERO, AccountType.CHECKING);

        StepVerifier.create(service.create(req))
                .expectErrorSatisfies(err -> {
                    var ex = (ResponseStatusException) err;
                    // tu servicio traduce 404 de ClientMS -> 400 BAD_REQUEST
                    assert ex.getStatusCode().value() == 400;
                })
                .verify();
    }

    @Test
    void create_uniqueNumber_retry_and_fail_afterAttempts_mapsTo409() {
        var service = new AccountService(repo, builderOk(), "http://fake-clientms");
        var req = new CreateAccountRequest("C1", BigDecimal.TEN, AccountType.SAVINGS);

        // siempre existe -> fuerza los 10 reintentos y termina en CONFLICT
        when(repo.existsByAccountNumber(any())).thenReturn(Mono.just(true));

        StepVerifier.create(service.create(req))
                .expectErrorSatisfies(err -> {
                    var ex = (ResponseStatusException) err;
                    assert ex.getStatusCode().value() == 409;
                })
                .verify();
    }

    @Test
    void listByClientId_callsValidation_andReturnsFlux() {
        var service = new AccountService(repo, builderOk(), "http://fake-clientms");

        var a1 = Account.builder().id("A1").clientId("C1").accountNumber("SV-000001")
                .balance(BigDecimal.ZERO).active(true).type(AccountType.SAVINGS).build();

        when(repo.findByClientId("C1")).thenReturn(Flux.just(a1));

        StepVerifier.create(service.listByClientId("C1"))
                .expectNext(a1)
                .verifyComplete();
    }

    @Test
    void listByClientId_clientMsDown_mapsTo503() {
        var service = new AccountService(repo, builder500(), "http://fake-clientms");

        StepVerifier.create(service.listByClientId("C1"))
                .expectErrorSatisfies(err -> {
                    var ex = (ResponseStatusException) err;
                    assert ex.getStatusCode().value() == 503; // SERVICE_UNAVAILABLE
                })
                .verify();
    }
}
