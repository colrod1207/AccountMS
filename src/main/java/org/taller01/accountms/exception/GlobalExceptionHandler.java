package org.taller01.AccountMS.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.taller01.AccountMS.dto.response.ErrorResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
      HttpServletRequest request) {
    log.warn("Resource not found: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.of(ex.getMessage(), "RESOURCE_NOT_FOUND",
        HttpStatus.NOT_FOUND.value(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException ex,
      HttpServletRequest request) {
    log.warn("Insufficient funds: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.of(ex.getMessage(), "INSUFFICIENT_FUNDS",
        HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(AccountInactiveException.class)
  public ResponseEntity<ErrorResponse> handleAccountInactive(AccountInactiveException ex,
      HttpServletRequest request) {
    log.warn("Account inactive: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.of(ex.getMessage(), "ACCOUNT_INACTIVE",
        HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
      HttpServletRequest request) {
    log.warn("Invalid argument: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.of(ex.getMessage(), "INVALID_ARGUMENT",
        HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex,
      HttpServletRequest request) {
    log.warn("Invalid state: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.of(ex.getMessage(), "INVALID_STATE",
        HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

    log.warn("Validation error: {}", errorMessage);
    ErrorResponse error = ErrorResponse.of("Validation failed: " + errorMessage, "VALIDATION_ERROR",
        HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex,
      HttpServletRequest request) {
    log.error("Unexpected error: ", ex);
    ErrorResponse error = ErrorResponse.of("An unexpected error occurred", "INTERNAL_SERVER_ERROR",
        HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
