// src/main/java/org/taller01/accountms/config/GlobalExceptionHandler.java
package org.taller01.accountms.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.taller01.accountms.exception.ApiError;
import org.taller01.accountms.exception.ResourceNotFoundException;
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> build(HttpStatus status, String msg, HttpServletRequest req, Map<String,String> fields) {
        return ResponseEntity.status(status).body(
                ApiError.builder()
                        .timestamp(Instant.now())
                        .status(status.value())
                        .error(status.is4xxClientError() ? "Solicitud incorrecta" : "Error del servidor")
                        .message(msg)
                        .path(req.getRequestURI())
                        .fieldErrors(fields)
                        .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> notFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> status(ResponseStatusException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return build(status, ex.getReason(), req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> beanValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String,String> fields = ex.getBindingResult().getFieldErrors()
                .stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b)->a));
        return build(HttpStatus.BAD_REQUEST, "Hay errores de validación en el cuerpo enviado", req, fields);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> badJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "JSON inválido o campos con formato incorrecto", req, null);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiError> duplicate(DuplicateKeyException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "El número de cuenta ya existe", req, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> unexpected(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado", req, null);
    }
}
