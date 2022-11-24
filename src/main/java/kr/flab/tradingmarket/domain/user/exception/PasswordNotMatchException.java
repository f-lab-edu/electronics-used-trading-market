package kr.flab.tradingmarket.domain.user.exception;

public class PasswordNotMatchException extends RuntimeException {

    public PasswordNotMatchException() {
    }

    public PasswordNotMatchException(String message) {
        super(message);
    }

    public PasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}