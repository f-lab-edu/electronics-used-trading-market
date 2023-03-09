package kr.flab.tradingmarket.domain.room.repository;

import static kr.flab.tradingmarket.domain.chat.util.RedisKeyUtils.*;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.domain.room.entity.RoomKey;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final RedisTemplate<String, String> redisChatTemplate;

    public Set<String> findByUserNo(Long userNo) {
        return redisChatTemplate.opsForSet().members(createUserRoomKey(userNo));
    }

    public boolean isRoomExists(String roomId) {
        return Boolean.TRUE.equals(redisChatTemplate.hasKey(createRoomKey(roomId)));
    }

    public boolean isRoomExistsByUserNoAndUserNo(String roomId, Long userNo) {
        return Boolean.TRUE.equals(redisChatTemplate.opsForSet().isMember(createUserRoomKey(userNo), roomId));
    }

    public String createRooms(RoomKey roomKey) {
        redisChatTemplate.opsForSet().add(createUserRoomKey(roomKey.getBuyerNo()), roomKey.getRoomId());
        redisChatTemplate.opsForSet().add(createUserRoomKey(roomKey.getSellerNo()), roomKey.getRoomId());
        return roomKey.getRoomId();
    }

}
