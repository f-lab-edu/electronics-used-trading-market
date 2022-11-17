package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;

public interface UserService {
    Long joinUser(JoinUserDto userDto);


    boolean isDuplicateUserId(String userId);

}
