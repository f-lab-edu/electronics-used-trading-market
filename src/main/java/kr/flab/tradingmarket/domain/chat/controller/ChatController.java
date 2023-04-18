package kr.flab.tradingmarket.domain.chat.controller;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static org.springframework.http.HttpStatus.*;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import kr.flab.tradingmarket.common.annotation.AuthCheck;
import kr.flab.tradingmarket.common.annotation.CurrentSession;
import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.chat.dto.reqeust.RequestChatMessageDto;
import kr.flab.tradingmarket.domain.chat.service.ChatFacadeService;
import kr.flab.tradingmarket.domain.chat.service.RedisChatSubscriber;
import kr.flab.tradingmarket.domain.room.entity.RoomKey;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChannelTopic topic;
    private final RedisChatSubscriber redisChatSubscriber;
    private final RedisMessageListenerContainer redisChatContainer;
    private final ChatFacadeService chatFacadeService;

    @GetMapping(value = "/streaming/{roomId}")
    @AuthCheck
    public SseEmitter streamMessage(@PathVariable String roomId, HttpServletResponse response,
        @CurrentSession Long userNo) {

        AtomicBoolean isComplete = new AtomicBoolean(false);

        RoomKey.isValid(roomId, userNo);

        //TODO timeout 성능 테스트 필요
        SseEmitter emitter = new SseEmitter(15000L);
        redisChatSubscriber.attach(emitter, roomId, userNo);

        Runnable onDetach = () -> {
            redisChatSubscriber.detach(emitter, roomId);
            if (!isComplete.get()) {
                isComplete.set(true);
                emitter.complete();
            }
        };

        emitter.onCompletion(onDetach);
        emitter.onTimeout(onDetach);
        emitter.onError((err) -> onDetach.run());
        response.setHeader("X-Accel-Buffering", "no");
        return emitter;
    }

    @PutMapping
    @AuthCheck
    public ResponseEntity<ResponseMessage> sendMessage(@RequestBody @Valid RequestChatMessageDto chatMessage,
        @CurrentSession Long userNo) {
        chatFacadeService.sendMessage(topic.getTopic(), chatMessage, userNo);
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }

}
