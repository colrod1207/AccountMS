// src/main/java/org/taller01/accountms/controller/AccountController.java
package org.taller01.accountms.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.taller01.accountms.domain.Account;
import org.taller01.accountms.dto.request.CreateAccountRequest;
import org.taller01.accountms.dto.request.UpdateAccountRequest;
import org.taller01.accountms.dto.response.AccountResponse;
import org.taller01.accountms.service.AccountService;
@RestController
@RequiredArgsConstructor
@RequestMapping("/cuentas")
public class AccountController {

  private final AccountService accountService;

  /** POST /cuentas — crea cuenta (accountType: SAVINGS | CHECKING) */
  @PostMapping
  public ResponseEntity<AccountResponse> crear(@Valid @RequestBody CreateAccountRequest req) {
    Account creada = accountService.create(req);
    return ResponseEntity
            .created(URI.create("/cuentas/" + creada.getId()))
            .body(AccountResponse.from(creada));
  }

  /** GET /cuentas — listar todas las cuentas */
  @GetMapping
  public ResponseEntity<List<AccountResponse>> listar() {
    var out = accountService.listAll()
            .stream().map(AccountResponse::from).toList();
    return ResponseEntity.ok(out);
  }

  /** GET /cuentas/{id} — obtener una cuenta por ID */
  @GetMapping("/{id}")
  public ResponseEntity<AccountResponse> obtener(@PathVariable String id) {
    return ResponseEntity.ok(AccountResponse.from(accountService.getById(id)));
  }

  /** GET /cuentas/cliente/{clientId} — listar cuentas por dueño (clientId) */
  @GetMapping("/cliente/{clientId}")
  public ResponseEntity<List<AccountResponse>> listarPorCliente(@PathVariable String clientId) {
    var out = accountService.listByClientId(clientId)
            .stream().map(AccountResponse::from).toList();
    return ResponseEntity.ok(out); // devuelve [] si no hay cuentas
  }

  /** PUT /cuentas/{id} — actualizar estado (ej. active) */
  @PutMapping("/{id}")
  public ResponseEntity<AccountResponse> actualizar(@PathVariable String id,
                                                    @Valid @RequestBody UpdateAccountRequest req) {
    Account actualizada = accountService.updatePut(id, req);
    return ResponseEntity.ok(AccountResponse.from(actualizada));
  }

  /** PATCH /cuentas/{id} — actualización parcial */
  @PatchMapping("/{id}")
  public ResponseEntity<AccountResponse> actualizarParcial(@PathVariable String id,
                                                           @RequestBody UpdateAccountRequest req) {
    Account actualizada = accountService.updatePatch(id, req);
    return ResponseEntity.ok(AccountResponse.from(actualizada));
  }

  /** DELETE /cuentas/{id} — eliminar cuenta */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable String id) {
    accountService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
