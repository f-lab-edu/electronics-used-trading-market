package kr.flab.tradingmarket.domain.product.exception;

public class ProductRegisterException extends RuntimeException {

    public ProductRegisterException() {
    }

    public ProductRegisterException(String message) {
        super(message);
    }

    public ProductRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

}
