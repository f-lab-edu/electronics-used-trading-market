package kr.flab.tradingmarket.domain.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.flab.tradingmarket.common.util.ObjectMapperUtils;
import kr.flab.tradingmarket.domain.chat.entity.ChatMessage;
import kr.flab.tradingmarket.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisChatService implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    public List<ChatMessage> findChatLogByRoomId(String roomId, int offset, int size) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<String> serializeMessages = chatRepository.getMessages(roomId, offset, size);
        for (String serializeMessage : serializeMessages) {
            chatMessages.add(ObjectMapperUtils.readValue(serializeMessage, ChatMessage.class));
        }
        return chatMessages;
    }

    @Override
    public void sendMessage(String topic, ChatMessage chatMessage) {
        chatRepository.saveMessage(chatMessage);
        chatRepository.sendMessage(topic, chatMessage);
    }

}
