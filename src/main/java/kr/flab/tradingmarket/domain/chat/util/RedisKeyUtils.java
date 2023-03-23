package kr.flab.tradingmarket.domain.chat.util;

public class RedisKeyUtils {

    public static final String ROOM_KEY = "room:%s";
    private static final String USER_ROOMS_KEY = "{user}:%d:{rooms}";

    /**
     * 채팅방 키 => room:구매자번호:판매자번호:상품번호
     * @param userNo
     * @param sellerNo
     * @param productNo
     * @return
     */
    public static String createRoomId(Long userNo, Long sellerNo, Long productNo) {
        return "%d:%d:%d".formatted(userNo, sellerNo, productNo);
    }

    public static String createRoomKey(String roomId) {
        return ROOM_KEY.formatted(roomId);
    }

    public static String createUserRoomKey(Long userId) {
        return USER_ROOMS_KEY.formatted(userId);
    }

}
