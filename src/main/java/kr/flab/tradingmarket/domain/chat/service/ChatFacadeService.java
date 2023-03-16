package kr.flab.tradingmarket.domain.chat.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import kr.flab.tradingmarket.domain.chat.dto.reqeust.RequestChatMessageDto;
import kr.flab.tradingmarket.domain.chat.entity.ChatMessage;

public interface ChatFacadeService {

    List<ChatMessage> findChatLogByRoomId(String roomId, int offset, int size);

    void sendMessage(String topic, @RequestBody RequestChatMessageDto chatMessage, Long userNo);
}
