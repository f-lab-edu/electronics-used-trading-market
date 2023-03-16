package kr.flab.tradingmarket.domain.room.controller;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static org.springframework.http.HttpStatus.*;

import javax.validation.constraints.Max;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.flab.tradingmarket.common.annotation.AuthCheck;
import kr.flab.tradingmarket.common.annotation.CurrentSession;
import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.chat.service.ChatFacadeService;
import kr.flab.tradingmarket.domain.room.entity.RoomKey;
import kr.flab.tradingmarket.domain.room.service.RoomFacadeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Validated
public class RoomsController {

    private final RoomFacadeService roomFacadeService;
    private final ChatFacadeService chatFacadeService;

    @GetMapping
    @AuthCheck
    public ResponseEntity<ResponseMessage> getRooms(@CurrentSession Long userNo) {
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(roomFacadeService.findRooms(userNo))
                .build());
    }

    @PutMapping(value = "/{sellerNo}/{productNo}")
    @AuthCheck
    public ResponseEntity<ResponseMessage> createRoom(@PathVariable Long sellerNo, @PathVariable Long productNo,
        @CurrentSession Long userNo) {
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(roomFacadeService.createRooms(RoomKey.of(userNo, sellerNo, productNo)))
                .build());
    }

    @GetMapping(value = "{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @AuthCheck
    public ResponseEntity<ResponseMessage> getMessages(@PathVariable String roomId, @CurrentSession Long userNo,
        @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") @Max(30) int size) {

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(chatFacadeService.findChatLogByRoomId(roomId, offset, size))
                .build());
    }
}