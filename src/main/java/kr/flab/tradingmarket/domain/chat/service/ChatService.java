package kr.flab.tradingmarket.domain.chat.service;

import java.util.List;

import kr.flab.tradingmarket.domain.chat.entity.ChatMessage;

public interface ChatService {
    List<ChatMessage> findChatLogByRoomId(String roomId, int offset, int size);

    void sendMessage(String topic, ChatMessage chatMessage);
}
