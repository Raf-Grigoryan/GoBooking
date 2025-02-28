package org.example.gobookingcommon.customException;

public class AccountNotVerifiedException extends RuntimeException {
  public AccountNotVerifiedException(String message) {
    super(message);
  }
}
