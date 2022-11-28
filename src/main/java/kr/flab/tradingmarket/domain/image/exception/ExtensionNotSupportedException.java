package kr.flab.tradingmarket.domain.image.exception;


public class ExtensionNotSupportedException extends RuntimeException {

    public ExtensionNotSupportedException() {
    }

    public ExtensionNotSupportedException(String message) {
        super(message);
    }

    public ExtensionNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}