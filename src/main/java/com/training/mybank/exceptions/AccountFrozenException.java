package com.training.mybank.exceptions;

public class AccountFrozenException extends BankingException {
    public AccountFrozenException(String message) {
        super(message);
    }
}
