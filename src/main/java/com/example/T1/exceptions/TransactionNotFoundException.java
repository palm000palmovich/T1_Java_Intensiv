package com.example.T1.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long tranId) {
        super("Транзакция с id " + tranId + " не найдена.");
    }
}
