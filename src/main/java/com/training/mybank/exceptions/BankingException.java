package com.training.mybank.exceptions;

public class BankingException extends RuntimeException {

    public BankingException(String message) {
        super(message);
    }
}
