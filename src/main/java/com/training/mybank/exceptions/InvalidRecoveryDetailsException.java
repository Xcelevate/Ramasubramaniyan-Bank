package com.training.mybank.exceptions;

public class InvalidRecoveryDetailsException extends BankingException {
    public InvalidRecoveryDetailsException(String message) {
        super(message);
    }
}
