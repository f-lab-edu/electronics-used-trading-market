package kr.flab.tradingmarket.domain.chat.service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import kr.flab.tradingmarket.common.util.ObjectMapperUtils;
import kr.flab.tradingmarket.domain.chat.entity.ChatMessage;
import kr.flab.tradingmarket.domain.chat.exception.ChatRoomNotFoundException;
import kr.flab.tradingmarket.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisChatSubscriber implements MessageListener {

    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitterTable = new ConcurrentHashMap<>();
    private final RoomService roomService;

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        String messageBody = new String(message.getBody());
        ChatMessage receiveMessage = ObjectMapperUtils.readValue(messageBody, ChatMessage.class);
        CopyOnWriteArrayList<SseEmitter> sseEmitters = emitterTable.get(receiveMessage.getRoomId());
        if (sseEmitters == null) {
            return;
        }

        for (SseEmitter emitter : sseEmitters) {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                .data(messageBody);
            try {
                emitter.send(event);
            } catch (IOException e) {
                detach(emitter, receiveMessage.getRoomId());
            }
        }
    }

    public void attach(SseEmitter emitter, String roomId, Long userNo) {
        if (!roomService.isRoomExistsByUserNoAndUserNo(roomId, userNo)) {
            throw new ChatRoomNotFoundException("ChatRoom Not Found : %s".formatted(roomId));
        }
        if (emitterTable.containsKey(roomId)) {
            emitterTable.get(roomId).add(emitter);
        }
        CopyOnWriteArrayList<SseEmitter> list = new CopyOnWriteArrayList<>();
        list.add(emitter);
        emitterTable.put(roomId, list);
    }

    public void detach(SseEmitter emitter, String roomId) {
        if (!emitterTable.containsKey(roomId)) {
            return;
        }
        Set<SseEmitter> set = ConcurrentHashMap.newKeySet();
        emitterTable.get(roomId).remove(emitter);
        if (emitterTable.get(roomId).size() == 0) {
            emitterTable.remove(roomId);
        }
    }

}