package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;

public interface UserService {
    Long joinUser(JoinUserDto userDto);

    boolean isDuplicateUserId(String userId);

    Long userAuthCheck(UserAuthDto userAuth);

    User findByUserId(String userId);

    User findByUserNo(Long userNo);

    void modifyUser(ModifyUserDto modifyUserDto, Long userId);

    void withdrawUser(Long userId);

    ModifyUserDto findModifyUserDtoByUserNo(Long userNo);

    String modifyUserProfile(UserProfileImage imagePath, Long userNo);

    void changePassword(ChangePasswordDto changePassword, Long userNo);
}
