package  com.training.mybank.exceptions;

import com.training.mybank.exceptions.BankingException;

public class AuthenticationFailedException extends BankingException {

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
