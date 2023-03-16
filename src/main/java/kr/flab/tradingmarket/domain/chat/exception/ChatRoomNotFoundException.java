package kr.flab.tradingmarket.domain.chat.exception;

public class ChatRoomNotFoundException extends RuntimeException {

    public ChatRoomNotFoundException() {
    }

    public ChatRoomNotFoundException(String message) {
        super(message);
    }

    public ChatRoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}