package kr.flab.tradingmarket.domain.user.exception;

public class UserIdDuplicateException extends RuntimeException {
    public UserIdDuplicateException() {
    }

    public UserIdDuplicateException(String message) {
        super(message);
    }

    public UserIdDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
