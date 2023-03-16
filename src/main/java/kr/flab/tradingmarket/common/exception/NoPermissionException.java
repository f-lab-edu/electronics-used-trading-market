package kr.flab.tradingmarket.common.exception;

public class NoPermissionException extends RuntimeException {

    public NoPermissionException() {
    }

    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

}
