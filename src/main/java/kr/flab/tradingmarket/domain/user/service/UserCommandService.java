package kr.flab.tradingmarket.domain.user.service;

import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.response.MyInfoDto;

public interface UserCommandService {
    void joinUser(JoinUserDto userDto);

    void modifyUser(ModifyUserDto modifyUserDto, Long userId);

    void withdrawUser(Long userId);

    MyInfoDto findModifyUserDtoByUserNo(Long userNo);

    void modifyUserProfile(Long userNo, MultipartFile image);

    void changePassword(ChangePasswordDto changePassword, Long userNo);
}
