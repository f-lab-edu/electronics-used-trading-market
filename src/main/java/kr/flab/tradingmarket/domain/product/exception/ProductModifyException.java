package kr.flab.tradingmarket.domain.product.exception;

public class ProductModifyException extends RuntimeException {

    public ProductModifyException() {
    }

    public ProductModifyException(String message) {
        super(message);
    }

    public ProductModifyException(String message, Throwable cause) {
        super(message, cause);
    }

}
