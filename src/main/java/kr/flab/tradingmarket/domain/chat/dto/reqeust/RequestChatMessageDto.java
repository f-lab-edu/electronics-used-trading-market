package kr.flab.tradingmarket.domain.chat.dto.reqeust;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kr.flab.tradingmarket.domain.chat.entity.ChatMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestChatMessageDto {

    @NotNull
    private Long to;
    @NotEmpty
    private String message;
    @NotEmpty
    private String roomId;
    @NotNull
    private Long from;

    public ChatMessage toEntity() {
        return new ChatMessage(this.from, this.to, this.message, this.roomId);
    }

    @AssertTrue(message = "잘못된 roomId 입니다.")
    private boolean isValidationRoomId() {
        String[] roomId = this.getRoomId().split(":");
        if (roomId.length != 3) {
            return false;
        }
        for (String id : roomId) {
            if (!id.matches("-?\\d+(\\.\\d+)?")) {
                return false;
            }
        }
        return true;
    }
}
