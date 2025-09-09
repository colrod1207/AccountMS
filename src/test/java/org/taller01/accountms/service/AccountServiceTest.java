package org.taller01.accountms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.taller01.accountms.domain.Account;
import org.taller01.accountms.domain.AccountType;
import org.taller01.accountms.dto.request.UpdateAccountRequest;
import org.taller01.accountms.repository.AccountRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository repo;
    private AccountService service;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(AccountRepository.class);
        // El baseUrl se usa en validarCliente(), pero en estos tests no lo invocamos.
        service = new AccountService(repo, WebClient.builder(), "http://localhost:8080");
    }

    private Account base() {
        return Account.builder()
                .id("A")
                .clientId("C1")
                .accountNumber("SV-000001")
                .balance(new BigDecimal("100.00"))
                .active(true)
                .type(AccountType.SAVINGS)
                .build();
    }

    @Test
    void deposit_increasesBalance() {
        var acc = base();
        when(repo.findById("A")).thenReturn(Mono.just(acc));
        when(repo.save(any(Account.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.deposit("A", new BigDecimal("50.00")))
                .expectNextMatches(saved -> saved.getBalance().compareTo(new BigDecimal("150.00")) == 0)
                .verifyComplete();

        verify(repo).save(any(Account.class));
    }

    @Test
    void withdraw_decreasesBalance() {
        var acc = base();
        when(repo.findById("A")).thenReturn(Mono.just(acc));
        when(repo.save(any(Account.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.withdraw("A", new BigDecimal("30.00")))
                .expectNextMatches(saved -> saved.getBalance().compareTo(new BigDecimal("70.00")) == 0)
                .verifyComplete();
    }

    @Test
    void withdraw_insufficientBalance_conflict409() {
        var acc = base();
        acc.setBalance(new BigDecimal("20.00"));
        when(repo.findById("A")).thenReturn(Mono.just(acc));

        StepVerifier.create(service.withdraw("A", new BigDecimal("30.00")))
                .expectErrorSatisfies(err -> {
                    var ex = (ResponseStatusException) err;
                    assert ex.getStatusCode().value() == 409;
                })
                .verify();
    }

    @Test
    void updatePut_toggleActive_ok() {
        var acc = base(); // active = true
        when(repo.findById("A")).thenReturn(Mono.just(acc));
        when(repo.save(any(Account.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.updatePut("A", new UpdateAccountRequest(false)))
                .expectNextMatches(saved -> saved.getActive().equals(false))
                .verifyComplete();
    }

    @Test
    void updatePut_sameValue_conflict409() {
        var acc = base(); // active = true
        when(repo.findById("A")).thenReturn(Mono.just(acc));

        StepVerifier.create(service.updatePut("A", new UpdateAccountRequest(true)))
                .expectErrorSatisfies(err -> {
                    var ex = (ResponseStatusException) err;
                    assert ex.getStatusCode().value() == 409;
                })
                .verify();
    }

    @Test
    void updatePatch_nullActive_badRequest400() {
        var acc = base();
        when(repo.findById("A")).thenReturn(Mono.just(acc));

        StepVerifier.create(service.updatePatch("A", new UpdateAccountRequest(null)))
                .expectErrorSatisfies(err -> {
                    var ex = (ResponseStatusException) err;
                    assert ex.getStatusCode().value() == 400;
                })
                .verify();
    }

    @Test
    void delete_nonZeroBalance_conflict409() {
        var acc = base(); // balance 100
        when(repo.findById("A")).thenReturn(Mono.just(acc));

        StepVerifier.create(service.delete("A"))
                .expectErrorSatisfies(err -> {
                    var ex = (ResponseStatusException) err;
                    assert ex.getStatusCode().value() == 409;
                })
                .verify();
    }

    @Test
    void delete_zeroBalance_ok() {
        var acc = base();
        acc.setBalance(BigDecimal.ZERO);
        when(repo.findById("A")).thenReturn(Mono.just(acc));
        when(repo.delete(acc)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete("A")).verifyComplete();
        verify(repo).delete(acc);
    }
}
