package kr.flab.tradingmarket.domain.chat.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    private Long from;
    private Long to;
    private Timestamp date;
    private String message;
    private String roomId;

    public ChatMessage(Long from, Long to, String message, String roomId) {
        this.from = from;
        this.to = to;
        this.date = Timestamp.valueOf(LocalDateTime.now());
        this.message = message;
        this.roomId = roomId;
    }
}
