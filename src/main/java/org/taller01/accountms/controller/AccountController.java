package org.taller01.accountms.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.taller01.accountms.dto.request.CreateAccountRequest;
import org.taller01.accountms.dto.request.UpdateAccountRequest;
import org.taller01.accountms.dto.response.AccountResponse;
import org.taller01.accountms.service.AccountService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cuentas")
public class AccountController {

  private final AccountService service;

  /** POST /cuentas — crea cuenta (accountType: SAVINGS | CHECKING) */
  @PostMapping
  public Mono<ResponseEntity<AccountResponse>> crear(@Valid @RequestBody CreateAccountRequest req) {
    return service.create(req).map(AccountResponse::from)
        .map(a -> ResponseEntity.created(URI.create("/cuentas/" + a.getId())).body(a));
  }

  /** GET /cuentas — listar todas */
  @GetMapping
  public Flux<AccountResponse> listar() {
    return service.listAll().map(AccountResponse::from);
  }

  /** GET /cuentas/{id} — obtener por ID */
  @GetMapping("/{id}")
  public Mono<AccountResponse> obtener(@PathVariable String id) {
    return service.getById(id).map(AccountResponse::from);
  }

  /** GET /cuentas/cliente/{clientId} — listar por cliente (valida en ClientMS) */
  @GetMapping("/cliente/{clientId}")
  public Flux<AccountResponse> listarPorCliente(@PathVariable String clientId) {
    return service.listByClientId(clientId).map(AccountResponse::from);
  }

  /** PUT /cuentas/{id} — actualizar 'active' (obligatorio) */
  @PutMapping("/{id}")
  public Mono<AccountResponse> actualizar(@PathVariable String id,
      @Valid @RequestBody UpdateAccountRequest req) {
    return service.updatePut(id, req).map(AccountResponse::from);
  }

  /** PATCH /cuentas/{id} — actualización parcial (active opcional) */
  @PatchMapping("/{id}")
  public Mono<AccountResponse> actualizarParcial(@PathVariable String id,
      @RequestBody UpdateAccountRequest req) {
    return service.updatePatch(id, req).map(AccountResponse::from);
  }

  /** DELETE /cuentas/{id} — no permite si saldo ≠ 0 */
  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id) {
    return service.delete(id).thenReturn(ResponseEntity.noContent().build());
  }

  // ============================================================
  // ENDPOINTS INTERNOS (usados por TransactionMS) — OCULTOS
  // ============================================================

  @Hidden
  @PostMapping("/internal/{id}/deposito")
  public Mono<AccountResponse> internalDeposit(@PathVariable String id,
      @RequestParam BigDecimal amount) {
    return service.deposit(id, amount).map(AccountResponse::from);
  }

  @Hidden
  @PostMapping("/internal/{id}/retiro")
  public Mono<AccountResponse> internalWithdraw(@PathVariable String id,
      @RequestParam BigDecimal amount) {
    return service.withdraw(id, amount).map(AccountResponse::from);
  }
}
