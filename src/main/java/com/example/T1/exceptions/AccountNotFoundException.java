package com.example.T1.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accId) {
      super("Аккаунт с id "  + accId + " не найден.");
    }
}
