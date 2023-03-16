package kr.flab.tradingmarket.domain.room.entity;

import java.util.Arrays;

import kr.flab.tradingmarket.common.exception.DtoValidationException;
import kr.flab.tradingmarket.common.exception.NoPermissionException;
import kr.flab.tradingmarket.domain.chat.util.RedisKeyUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RoomKey {
    private final Long buyerNo;
    private final Long sellerNo;
    private final Long productNo;
    private final String roomId;

    public static RoomKey of(Long userNo, Long sellerNo, Long productNo) {
        return new RoomKey(userNo, sellerNo, productNo, RedisKeyUtils.createRoomId(userNo, sellerNo, productNo));
    }

    public static RoomKey from(String roomId) {
        long[] id = Arrays.stream(roomId.split(":")).mapToLong(Long::valueOf).toArray();
        return new RoomKey(id[0], id[1], id[2], roomId);
    }

    public static void isValid(String roomId, Long userNo) {
        long[] id = Arrays.stream(roomId.split(":")).mapToLong(Long::valueOf).toArray();
        RoomKey roomKey = new RoomKey(id[0], id[1], id[2], roomId);
        if (!(roomKey.getBuyerNo().equals(userNo) || roomKey.getSellerNo().equals(userNo))) {
            throw new NoPermissionException("wrong user access");
        }
    }

    public void isValid(Long sellerNo, Long productNo) {
        if (!this.sellerNo.equals(sellerNo) || !this.productNo.equals(productNo)) {
            throw new DtoValidationException("seller / product", "잘못된 접근 입니다.");
        }
    }

    public String getRoomId() {
        return this.roomId;
    }

}
