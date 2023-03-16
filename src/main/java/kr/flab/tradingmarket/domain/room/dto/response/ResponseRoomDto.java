package kr.flab.tradingmarket.domain.room.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseRoomDto {
    private String roomId;
    private Long productId;
    private String sellerName;
    private String productName;

    public ResponseRoomDto(String roomId, Long productId, String sellerName, String productName) {
        this.roomId = roomId;
        this.productId = productId;
        this.sellerName = sellerName;
        this.productName = productName;
    }
}
