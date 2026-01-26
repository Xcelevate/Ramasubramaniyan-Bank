package  com.training.mybank.exceptions;
public class AccountInactiveException extends BankingException {

    public AccountInactiveException(String message) {
        super(message);
    }
}
