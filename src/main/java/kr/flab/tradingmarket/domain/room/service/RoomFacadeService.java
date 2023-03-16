package kr.flab.tradingmarket.domain.room.service;

import java.util.List;

import kr.flab.tradingmarket.domain.room.dto.response.ResponseRoomDto;
import kr.flab.tradingmarket.domain.room.entity.RoomKey;

public interface RoomFacadeService {

    List<ResponseRoomDto> findRooms(Long userNo);

    String createRooms(RoomKey roomKey);

}
