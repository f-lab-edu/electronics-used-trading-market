package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.entity.User;

public interface UserService {
    Long joinUser(JoinUserDto userDto);


    boolean isDuplicateUserId(String userId);

    Long userAuthCheck(UserAuthDto userAuth);

    User findByUserId(String userId);

    User findByUserNo(Long userNo);

    void modifyUser(ModifyUserDto modifyUserDto, Long userId);

    void withdrawUser(Long userId);

    ModifyUserDto findModifyUserDtoByUserNo(Long userNo);
}
