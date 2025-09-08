package org.taller01.AccountMS.controller;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taller01.AccountMS.domain.Account;
import org.taller01.AccountMS.dto.request.BalanceOperationRequest;
import org.taller01.AccountMS.dto.request.CreateAccountRequest;
import org.taller01.AccountMS.dto.response.AccountResponse;
import org.taller01.AccountMS.dto.response.ErrorResponse;
import org.taller01.AccountMS.service.AccountService;

@RestController
@RequestMapping("cuentas")
@AllArgsConstructor
@Tag(name = "Account Management", description = "API para gestión de cuentas bancarias")
public class AccountController {

  private final AccountService accountService;

  @GetMapping
  @Operation(summary = "Listar todas las cuentas",
      description = "Obtiene una lista de todas las cuentas bancarias")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de cuentas obtenida exitosamente",
          content = @Content(schema = @Schema(implementation = AccountResponse.class)))})
  public ResponseEntity<List<AccountResponse>> list() {
    return ResponseEntity.ok(
        accountService.listAll().stream().map(AccountResponse::from).collect(Collectors.toList()));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener cuenta por ID",
      description = "Obtiene los detalles de una cuenta específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cuenta encontrada",
          content = @Content(schema = @Schema(implementation = AccountResponse.class))),
      @ApiResponse(responseCode = "404", description = "Cuenta no encontrada",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public ResponseEntity<AccountResponse> getById(
      @Parameter(description = "ID único de la cuenta", required = true) @PathVariable String id) {
    Account account = accountService.getById(id);
    return ResponseEntity.ok(AccountResponse.from(account));
  }

  @PostMapping
  @Operation(summary = "Crear nueva cuenta", description = "Crea una nueva cuenta bancaria")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente",
          content = @Content(schema = @Schema(implementation = AccountResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public ResponseEntity<AccountResponse> createAccount(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Datos para crear la cuenta", required = true)
      @Valid @RequestBody CreateAccountRequest request) {
    Account account = accountService.createAccount(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(AccountResponse.from(account));
  }

  @PutMapping("/{cuentaId}/depositar")
  @Operation(summary = "Realizar depósito",
      description = "Deposita dinero en una cuenta específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Depósito realizado exitosamente",
          content = @Content(schema = @Schema(implementation = AccountResponse.class))),
      @ApiResponse(responseCode = "400", description = "Cuenta inactiva o monto inválido",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Cuenta no encontrada",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public ResponseEntity<AccountResponse> deposit(
      @Parameter(description = "ID de la cuenta", required = true) @PathVariable String cuentaId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Monto a depositar",
          required = true)
      @Valid @RequestBody BalanceOperationRequest request) {
    Account account = accountService.deposit(cuentaId, request.amount());
    return ResponseEntity.ok(AccountResponse.from(account));
  }

  @PutMapping("/{cuentaId}/retirar")
  @Operation(summary = "Realizar retiro", description = "Retira dinero de una cuenta específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Retiro realizado exitosamente",
          content = @Content(schema = @Schema(implementation = AccountResponse.class))),
      @ApiResponse(responseCode = "400",
          description = "Fondos insuficientes, cuenta inactiva o monto inválido",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Cuenta no encontrada",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public ResponseEntity<AccountResponse> withdraw(
      @Parameter(description = "ID de la cuenta", required = true) @PathVariable String cuentaId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Monto a retirar",
          required = true)
      @Valid @RequestBody BalanceOperationRequest request) {
    Account account = accountService.withdraw(cuentaId, request.amount());
    return ResponseEntity.ok(AccountResponse.from(account));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar cuenta",
      description = "Elimina una cuenta bancaria (solo si el balance es cero)")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
          @ApiResponse(responseCode = "400",
              description = "No se puede eliminar cuenta con balance no-cero",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "404", description = "Cuenta no encontrada",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public ResponseEntity<Void> deleteAccount(
      @Parameter(description = "ID único de la cuenta", required = true) @PathVariable String id) {
    accountService.deleteAccount(id);
    return ResponseEntity.noContent().build();
  }
}
