package kr.flab.tradingmarket.domain.room.repository;

import static kr.flab.tradingmarket.domain.chat.util.RedisKeyUtils.*;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.domain.room.entity.RoomKey;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DefaultRoomRepository implements RoomRepository {

    private final RedisTemplate<String, String> redisChatTemplate;

    @Override
    public Set<String> findByUserNo(Long userNo) {
        return redisChatTemplate.opsForSet().members(createUserRoomKey(userNo));
    }

    @Override
    public boolean isRoomExists(String roomId) {
        return Boolean.TRUE.equals(redisChatTemplate.hasKey(createRoomKey(roomId)));
    }

    @Override
    public boolean isRoomExistsByUserNoAndUserNo(String roomId, Long userNo) {
        return Boolean.TRUE.equals(redisChatTemplate.opsForSet().isMember(createUserRoomKey(userNo), roomId));
    }

    @Override
    public String createRooms(RoomKey roomKey) {
        redisChatTemplate.opsForSet().add(createUserRoomKey(roomKey.getBuyerNo()), roomKey.getRoomId());
        redisChatTemplate.opsForSet().add(createUserRoomKey(roomKey.getSellerNo()), roomKey.getRoomId());
        return roomKey.getRoomId();
    }

}
