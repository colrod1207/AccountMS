package org.taller01.accountms.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taller01.accountms.dto.response.AccountResponse;
import org.taller01.accountms.service.AccountService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cuentas")
@AllArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping
  public ResponseEntity<List<AccountResponse>> list() {
    return ResponseEntity.ok(
        accountService.listAll().stream().map(AccountResponse::from).collect(Collectors.toList()));
  }
}
