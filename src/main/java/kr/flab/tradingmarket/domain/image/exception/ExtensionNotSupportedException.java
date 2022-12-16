package kr.flab.tradingmarket.domain.image.exception;


public class ExtensionNotSupportedException extends RuntimeException {
    String extension;

    public ExtensionNotSupportedException(String message, String extension) {
        super(message);
        this.extension = extension;
    }

    public ExtensionNotSupportedException(String message) {
        super(message);
    }

    public ExtensionNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getExtension() {
        return extension;
    }
}