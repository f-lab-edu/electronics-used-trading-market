package kr.flab.tradingmarket.domain.user.exception;

public class UserAccessDeniedException extends RuntimeException {

    public UserAccessDeniedException() {
    }

    public UserAccessDeniedException(String message) {
        super(message);
    }

    public UserAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}