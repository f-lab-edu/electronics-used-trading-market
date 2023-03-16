package kr.flab.tradingmarket.domain.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import kr.flab.tradingmarket.domain.chat.dto.reqeust.RequestChatMessageDto;
import kr.flab.tradingmarket.domain.chat.entity.ChatMessage;
import kr.flab.tradingmarket.domain.chat.exception.ChatRoomNotFoundException;
import kr.flab.tradingmarket.domain.room.entity.RoomKey;
import kr.flab.tradingmarket.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultChatFacadeService implements ChatFacadeService {

    private final ChatService chatService;
    private final RoomService roomService;

    @Override
    public List<ChatMessage> findChatLogByRoomId(String roomId, int offset, int size) {
        if (!roomService.isRoomExists(roomId)) {
            throw new ChatRoomNotFoundException("ChatRoom Not Found : %s".formatted(roomId));
        }
        return chatService.findChatLogByRoomId(roomId, offset, size);
    }

    @Override
    public void sendMessage(String topic, @RequestBody RequestChatMessageDto chatMessage, Long userNo) {
        ChatMessage message = chatMessage.toEntity();
        RoomKey.isValid(chatMessage.getRoomId(), userNo);
        if (!roomService.isRoomExistsByUserNoAndUserNo(message.getRoomId(), userNo)) {
            throw new ChatRoomNotFoundException("ChatRoom Not Found : %s".formatted(message.getRoomId()));
        }
        chatService.sendMessage(topic, message);
    }
}
