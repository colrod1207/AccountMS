package org.taller01.accountms.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.taller01.accountms.domain.Account;
import org.taller01.accountms.domain.AccountType;
import org.taller01.accountms.dto.request.CreateAccountRequest;
import org.taller01.accountms.dto.response.AccountResponse;
import org.taller01.accountms.service.AccountService;

import java.math.BigDecimal;

@WebFluxTest(controllers = AccountController.class)
class AccountControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private AccountService service;

    private Account acc(String id, String num, double bal, AccountType type) {
        return Account.builder()
                .id(id)
                .clientId("C1")
                .accountNumber(num)
                .balance(BigDecimal.valueOf(bal))
                .active(true)
                .type(type)
                .build();
    }

    @Test
    void listAll_ok() {
        Mockito.when(service.listAll()).thenReturn(Flux.just(
                acc("A1","SV-000001",10.0,AccountType.SAVINGS),
                acc("A2","CH-000002",0.0,AccountType.CHECKING)
        ));

        client.get().uri("/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBodyList(AccountResponse.class)
                .hasSize(2);
    }

    @Test
    void create_returns201() {
        var req = new CreateAccountRequest("C1", BigDecimal.valueOf(100.00), AccountType.SAVINGS);
        var saved = acc("A1","SV-123456",100.0,AccountType.SAVINGS);

        Mockito.when(service.create(Mockito.any(CreateAccountRequest.class)))
                .thenReturn(Mono.just(saved));

        client.post().uri("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
            {"clientId":"C1","initialBalance":100.00,"accountType":"SAVINGS"}
            """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location",".*/cuentas/A1$")
                .expectBody(AccountResponse.class)
                .consumeWith(b -> {
                    var body = b.getResponseBody();
                    assert body != null && body.getId().equals("A1");
                });
    }

    @Test
    void delete_returns204() {
        Mockito.when(service.delete("A1")).thenReturn(Mono.empty());

        client.delete().uri("/cuentas/A1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
