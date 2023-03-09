package kr.flab.tradingmarket.domain.room.repository;

import java.util.Set;

import kr.flab.tradingmarket.domain.room.entity.RoomKey;

public interface RoomRepository {
    Set<String> findByUserNo(Long userNo);

    boolean isRoomExists(String roomId);

    boolean isRoomExistsByUserNoAndUserNo(String roomId, Long userNo);

    String createRooms(RoomKey roomKey);
}
