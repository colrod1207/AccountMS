// src/main/java/org/taller01/accountms/service/AccountService.java
package org.taller01.accountms.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.taller01.accountms.domain.*;
import org.taller01.accountms.dto.request.CreateAccountRequest;
import org.taller01.accountms.dto.request.UpdateAccountRequest;
import org.taller01.accountms.exception.ResourceNotFoundException;
import org.taller01.accountms.repository.AccountRepository;
@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final RestTemplate restTemplate;

  @Value("${clientms.base-url}")
  private String clientBaseUrl;

  private static final SecureRandom RNG = new SecureRandom();

  // ---------- LECTURAS ----------
  @Transactional(readOnly = true)
  public List<Account> listAll() {
    return accountRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Account> listByClientId(String clientId) {
    // VALIDAMOS CONTRA ClientMS: si id inválido o inexistente => 400 con mensaje claro
    validarCliente(clientId);
    return accountRepository.findByClientId(clientId);
  }

  @Transactional(readOnly = true)
  public Account getById(String id) {
    return accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
  }

  // ---------- ESCRITURAS ----------
  @Transactional
  public Account create(CreateAccountRequest req) {
    validarCliente(req.clientId());
    String accountNumber = generarNumeroCuentaUnico(req.accountType());

    Account acc = Account.builder()
            .clientId(req.clientId())
            .accountNumber(accountNumber)
            .balance(req.initialBalance() == null ? BigDecimal.ZERO : req.initialBalance())
            .active(true)
            .type(req.accountType())
            .build();

    return accountRepository.save(acc);
  }

  @Transactional
  public Account updatePut(String id, UpdateAccountRequest req) {
    Account acc = getById(id);
    if (req.active() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'active' es obligatorio");
    }
    if (req.active().equals(acc.getActive())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
              acc.getActive() != null && acc.getActive() ? "La cuenta ya está activada" : "La cuenta ya está desactivada");
    }
    acc.setActive(req.active());
    return accountRepository.save(acc);
  }

  @Transactional
  public Account updatePatch(String id, UpdateAccountRequest req) {
    Account acc = getById(id);
    if (req.active() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se envió ningún cambio");
    }
    if (req.active().equals(acc.getActive())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
              acc.getActive() ? "La cuenta ya está activada" : "La cuenta ya está desactivada");
    }
    acc.setActive(req.active());
    return accountRepository.save(acc);
  }

  @Transactional
  public void delete(String id) {
    Account acc = getById(id);
    if (acc.getBalance() != null && acc.getBalance().compareTo(BigDecimal.ZERO) != 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
              "No se puede eliminar la cuenta: el saldo debe ser 0.00");
    }
    accountRepository.delete(acc);
  }

  // ---------- helpers ----------
  private void validarCliente(String clientId) {
    String base = clientBaseUrl.endsWith("/") ? clientBaseUrl.substring(0, clientBaseUrl.length()-1) : clientBaseUrl;
    String url = base + "/clientes/" + clientId;
    try {
      restTemplate.getForEntity(url, Object.class);
    } catch (HttpClientErrorException e) {
      int code = e.getStatusCode().value();
      if (code == 404) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente no existe");
      } else if (code == 400) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El identificador de cliente es inválido");
      } else if (e.getStatusCode().is4xxClientError()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo validar el cliente");
      } else {
        // 5xx de ClientMS
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "ClientMS no disponible");
      }
    } catch (RestClientException e) {
      // timeouts, DNS, etc.
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "ClientMS no disponible");
    }
  }

  private String generarNumeroCuentaUnico(AccountType type) {
    String pref = (type == AccountType.SAVINGS) ? "SV" : "CH";
    for (int i = 0; i < 20; i++) {
      String cand = String.format("%s-%06d", pref, RNG.nextInt(1_000_000));
      if (!accountRepository.existsByAccountNumber(cand)) return cand;
    }
    throw new ResponseStatusException(HttpStatus.CONFLICT, "No se pudo generar un número de cuenta único");
  }
}
