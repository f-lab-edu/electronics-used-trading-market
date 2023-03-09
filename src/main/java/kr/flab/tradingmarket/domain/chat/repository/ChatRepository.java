package kr.flab.tradingmarket.domain.chat.repository;

import java.util.List;

import kr.flab.tradingmarket.domain.chat.entity.ChatMessage;

public interface ChatRepository {
    List<String> getMessages(String roomId, int offset, int size);

    void sendMessage(String topic, ChatMessage message);

    void saveMessage(ChatMessage message);
}
