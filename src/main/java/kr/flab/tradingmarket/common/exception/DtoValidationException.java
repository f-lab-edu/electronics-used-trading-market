package kr.flab.tradingmarket.common.exception;

public class DtoValidationException extends RuntimeException {
    private final String field;

    public DtoValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public DtoValidationException(String field, String message, Throwable cause) {
        super(message, cause);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
