package kr.flab.tradingmarket.domain.chat.repository;

import static kr.flab.tradingmarket.domain.chat.util.RedisKeyUtils.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.common.util.ObjectMapperUtils;
import kr.flab.tradingmarket.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRepository {

    private final RedisTemplate<String, String> redisChatTemplate;

    public List<String> getMessages(String roomId, int offset, int size) {
        String roomNameKey = createRoomKey(roomId);
        List<String> messages = redisChatTemplate.opsForList().range(roomNameKey, offset, offset + size);
        if (messages == null) {
            return null;
        }
        Collections.reverse(messages);
        return messages;
    }

    public void sendMessage(String topic, ChatMessage message) {
        redisChatTemplate.convertAndSend(topic, ObjectMapperUtils.writeValueAsString(message));
    }

    public void saveMessage(ChatMessage message) {
        String roomKey = createRoomKey(message.getRoomId());
        redisChatTemplate.opsForList().rightPush(roomKey, ObjectMapperUtils.writeValueAsString(message));
        redisChatTemplate.expire(roomKey, 14, TimeUnit.DAYS);
    }

}
