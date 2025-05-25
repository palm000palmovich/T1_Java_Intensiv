package com.example.T1.dto;

import com.example.T1.enums.Type;
import com.example.T1.model.Account;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AccountDto {
    @Enumerated(EnumType.STRING)
    private Type accountType;
    private Long balance;

    public AccountDto(Long clientId, Type accountType, Long balance) {
        this.accountType = accountType;
        this.balance = balance;
    }

    public AccountDto(){}

    public Type getAccountType() {
        return accountType;
    }

    public void setAccountType(Type accountType) {
        this.accountType = accountType;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                ", accountType=" + accountType +
                ", balance=" + balance +
                '}';
    }
}
