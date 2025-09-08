package org.taller01.AccountMS.exception;

public class AccountInactiveException extends RuntimeException {
  public AccountInactiveException(String message) {
    super(message);
  }
}
