package kr.flab.tradingmarket.domain.chat.service;

import static kr.flab.tradingmarket.domain.chat.util.RedisKeyUtils.*;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private final RedisTemplate<String, String> redisChatTemplate;

    public RedisKeyExpiredListener(@Qualifier("redisChatContainer") RedisMessageListenerContainer listenerContainer,
        @Qualifier("redisChatTemplate") RedisTemplate<String, String> redisChatTemplate) {
        super(listenerContainer);
        this.redisChatTemplate = redisChatTemplate;
    }

    private long[] parseKeys(Message message) {
        return Arrays.stream(message.toString().split(":"))
            .filter(i -> i.matches("-?\\d+(\\.\\d+)?"))
            .mapToLong(Long::valueOf)
            .toArray();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        long[] keys = parseKeys(message);
        String roomId = createRoomId(keys[0], keys[1], keys[2]);
        redisChatTemplate.opsForSet().remove(createUserRoomKey(keys[0]), roomId);
        redisChatTemplate.opsForSet().remove(createUserRoomKey(keys[1]), roomId);
    }
}
