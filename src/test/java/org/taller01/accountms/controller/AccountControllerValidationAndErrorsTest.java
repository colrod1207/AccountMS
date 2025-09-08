package org.taller01.accountms.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import org.springframework.web.server.ResponseStatusException;
import org.taller01.accountms.domain.Account;
import org.taller01.accountms.domain.AccountType;
import org.taller01.accountms.dto.response.AccountResponse;
import org.taller01.accountms.exception.GlobalExceptionHandler;
import org.taller01.accountms.exception.ApiError;
import org.taller01.accountms.exception.ResourceNotFoundException;
import org.taller01.accountms.service.AccountService;

@WebFluxTest(controllers = AccountController.class)
@Import(GlobalExceptionHandler.class)
class AccountControllerValidationAndErrorsTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private AccountService service;

    private Account acc(String id, String num, double bal, AccountType type) {
        return Account.builder().id(id).clientId("C1").accountNumber(num)
                .balance(BigDecimal.valueOf(bal)).active(true).type(type).build();
    }

    @Test
    void create_validationError_returns400_withFieldErrors() {
        // clientId vacío y saldo negativo => WebExchangeBindException (400)
        client.post().uri("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
            {"clientId":"","initialBalance":-5,"accountType":"SAVINGS"}
            """)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiError.class)
                .consumeWith(r -> {
                    var body = r.getResponseBody();
                    assert body != null;
                    assert body.getStatus() == 400;
                    assert body.getMessage().contains("validación");
                    assert body.getFieldErrors() != null && !body.getFieldErrors().isEmpty();
                });
    }

    @Test
    void create_invalidEnum_returns400_serverWebInputException() {
        // accountType inválido => ServerWebInputException (400)
        client.post().uri("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
            {"clientId":"C1","initialBalance":0,"accountType":"SAVINGSSS"}
            """)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiError.class)
                .consumeWith(r -> {
                    var body = r.getResponseBody();
                    assert body != null && body.getStatus() == 400;
                    assert body.getMessage().toLowerCase().contains("json");
                });
    }

    @Test
    void obtener_notFound_mapsTo404() {
        Mockito.when(service.getById("NOPE"))
                .thenReturn(Mono.error(new ResourceNotFoundException("Cuenta no encontrada")));

        client.get().uri("/cuentas/NOPE")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .consumeWith(r -> {
                    var body = r.getResponseBody();
                    assert body != null && body.getStatus() == 404;
                });
    }

    @Test
    void listarPorCliente_ok() {
        Mockito.when(service.listByClientId("C1"))
                .thenReturn(Flux.just(acc("A1","SV-000001",10,AccountType.SAVINGS)));

        client.get().uri("/cuentas/cliente/C1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountResponse.class)
                .hasSize(1);
    }

    @Test
    void put_conflict409_bubbledFromService() {
        Mockito.when(service.updatePut(Mockito.eq("A1"), Mockito.any()))
                .thenReturn(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "conflicto")));

        client.put().uri("/cuentas/A1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
            {"active": true}
            """)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ApiError.class)
                .consumeWith(r -> {
                    var body = r.getResponseBody();
                    assert body != null && body.getStatus() == 409;
                });
    }

    @Test
    void internal_deposit_ok_hiddenButReachable() {
        Mockito.when(service.deposit("A1", new BigDecimal("50")))
                .thenReturn(Mono.just(acc("A1","SV-000001",150,AccountType.SAVINGS)));

        client.post().uri(uriBuilder -> uriBuilder
                        .path("/cuentas/internal/{id}/deposito").queryParam("amount","50").build("A1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .consumeWith(r -> {
                    var body = r.getResponseBody();
                    assert body != null && body.getId().equals("A1");
                });
    }
}
