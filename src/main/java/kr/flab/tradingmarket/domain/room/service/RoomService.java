package kr.flab.tradingmarket.domain.room.service;

import java.util.Set;

import kr.flab.tradingmarket.domain.room.entity.RoomKey;

public interface RoomService {
    String createRooms(RoomKey roomKey);

    Set<String> findByUserId(Long userNo);

    boolean isRoomExists(String roomId);

    boolean isRoomExistsByUserNoAndUserNo(String roomId, Long userNo);

}