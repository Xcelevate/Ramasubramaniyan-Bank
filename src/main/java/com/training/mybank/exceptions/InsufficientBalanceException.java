package com.training.mybank.exceptions;

public class InsufficientBalanceException extends BankingException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
