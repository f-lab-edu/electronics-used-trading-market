package kr.flab.tradingmarket.domain.image.exception;

public class ImageUploadException extends RuntimeException {

    public ImageUploadException() {
    }

    public ImageUploadException(String message) {
        super(message);
    }

    public ImageUploadException(String message, Throwable cause) {
    }

}
