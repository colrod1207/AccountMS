package org.taller01.accountms.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private MockServerWebExchange ex(String path) {
        return MockServerWebExchange.from(MockServerHttpRequest.get(path).build());
    }

    @Test
    void notFound_builds404() {
        ResponseEntity<ApiError> r = handler.notFound(
                new ResourceNotFoundException("no existe"),
                ex("/cuentas/abc")
        );
        assertEquals(404, r.getStatusCode().value());
        assertEquals("/cuentas/abc", r.getBody().getPath());
        assertEquals("no existe", r.getBody().getMessage());
    }

    @Test
    void status_usesProvidedStatus() {
        ResponseEntity<ApiError> r = handler.status(
                new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "ya existe"),
                ex("/cuentas")
        );
        assertEquals(409, r.getStatusCode().value());
        assertEquals("ya existe", r.getBody().getMessage());
    }

    @Test
    void badInput_400() {
        ResponseEntity<ApiError> r = handler.badInput(
                new ServerWebInputException("json malo"),
                ex("/cuentas")
        );
        assertEquals(400, r.getStatusCode().value());
        assertEquals("/cuentas", r.getBody().getPath());
    }

    @Test
    void duplicateKey_409() {
        ResponseEntity<ApiError> r = handler.duplicate(
                new DuplicateKeyException("dup"),
                ex("/cuentas")
        );
        assertEquals(409, r.getStatusCode().value());
    }

    @Test
    void unexpected_500() {
        ResponseEntity<ApiError> r = handler.unexpected(
                new RuntimeException("boom"),
                ex("/x")
        );
        assertEquals(500, r.getStatusCode().value());
        assertEquals("/x", r.getBody().getPath());
    }
}
