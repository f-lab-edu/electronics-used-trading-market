package kr.flab.tradingmarket.domain.room.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.flab.tradingmarket.domain.room.entity.RoomKey;
import kr.flab.tradingmarket.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultRoomService implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public String createRooms(RoomKey roomKey) {
        return roomRepository.createRooms(roomKey);
    }

    @Override
    public Set<String> findByUserId(Long userNo) {
        return roomRepository.findByUserNo(userNo);
    }

    @Override
    public boolean isRoomExists(String roomId) {
        return roomRepository.isRoomExists(roomId);
    }

    @Override
    public boolean isRoomExistsByUserNoAndUserNo(String roomId, Long userNo) {
        return roomRepository.isRoomExistsByUserNoAndUserNo(roomId, userNo);
    }

}
