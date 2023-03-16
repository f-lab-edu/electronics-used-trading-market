package kr.flab.tradingmarket.domain.user.dto.response;

import kr.flab.tradingmarket.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyInfoDto {

    private String userName;
    private String userPhone;

    static public MyInfoDto from(User user) {
        return MyInfoDto.builder()
            .userName(user.getUserName())
            .userPhone(user.getUserPhone())
            .build();
    }

}
