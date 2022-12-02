package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.response.MyInfoDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static kr.flab.tradingmarket.domain.user.service.UserServiceTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {


    @InjectMocks
    DefaultUserService userService;
    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    LoginService loginService;


    @Test
    @DisplayName("service : 회원가입 테스트 : 성공")
    void joinUserSuccess() {
        //given
        given(userMapper.findById(SUCCESSFUL_JOIN_USER.getUserId())).willReturn(null);
        given(userMapper.insertUser(SUCCESSFUL_JOIN_USER)).willReturn(1L);
        given(userMapper.findByNo(1L)).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        //when
        Long userId = userService.joinUser(SUCCESSFUL_JOIN_USER_DTO);
        User findUser = userMapper.findByNo(userId);
        //then
        assertThat(userId).isEqualTo(1L);
        assertThat(findUser).isEqualTo(SUCCESSFUL_JOIN_RETURN_USER);
    }

    @Test
    @DisplayName("service : 회원가입 테스트 : 아이디 중복으로 인한 회원가입 실패")
    void joinUserFail() {
        //given
        given(userMapper.findById(SUCCESSFUL_JOIN_USER.getUserId())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        //when
        //then
        assertThatThrownBy(() -> userService.joinUser(SUCCESSFUL_JOIN_USER_DTO)).isInstanceOf(UserIdDuplicateException.class);

    }

    @Test
    @DisplayName("service : id 중복확인 테스트 : 중복된사용자 있음")
    void isDuplicateUserIdTrue() {
        //given
        given(userMapper.findById(SUCCESSFUL_JOIN_USER.getUserId())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        //when
        boolean result = userService.isDuplicateUserId(SUCCESSFUL_JOIN_USER_DTO.getUserId());
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("service : id 중복확인 테스트 : 중복된사용자 없음")
    void isDuplicateUserIdFalse() {
        //given
        given(userMapper.findById(SUCCESSFUL_JOIN_USER.getUserId())).willReturn(null);
        //when
        boolean result = userService.isDuplicateUserId(SUCCESSFUL_JOIN_USER_DTO.getUserId());
        //then
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("service : 유저 아이디로 유저찾기 : 성공")
    void successfulFindByUserId() {
        //given
        given(userMapper.findById(any())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        //when
        User findUser = userService.findByUserId(SUCCESSFUL_JOIN_RETURN_USER.getUserId());
        //then
        assertThat(findUser).isEqualTo(SUCCESSFUL_JOIN_RETURN_USER);


    }


    @Test
    @DisplayName("service : 유저 아이디로 유저찾기 : 해당유저가 없을때 실패 ")
    void failFindByUserId() {
        //given
        given(userMapper.findById(any())).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> userService.findByUserId(any()))
                .isInstanceOf(UserNotFoundException.class);
    }


    @Test
    @DisplayName("service : 유저 번호로 유저 찾기 : 성공")
    void successfulFindByUserNo() {
        //given
        given(userMapper.findByNo(any())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        //when
        User findUser = userService.findByUserNo(SUCCESSFUL_JOIN_RETURN_USER.getUserNo());
        //then
        assertThat(findUser).isEqualTo(SUCCESSFUL_JOIN_RETURN_USER);

    }


    @Test
    @DisplayName("service : 유저 번호 유저찾기 : 해당유저가 없을때 실패 ")
    void failFindByUserNo() {
        //given
        given(userMapper.findByNo(any())).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> userService.findByUserNo(any()))
                .isInstanceOf(UserNotFoundException.class);
    }


    @Test
    @DisplayName("service : user 인증 정보확인 테스트 : 성공")
    void successfulUserAuthCheck() {
        //given
        given(userMapper.findById(any())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        //when
        Long returnUserNo = userService.userAuthCheck(SUCCESSFUL_USER_AUTH_DTO);

        //then
        assertThat(returnUserNo).isEqualTo(1L);
    }


    @Test
    @DisplayName("service : user 인증 정보확인 테스트 : 비밀번호 오류 실패")
    void failUserAuthCheck() {
        //given
        given(userMapper.findById(any())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        given(passwordEncoder.matches(any(), any())).willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> userService.userAuthCheck(SUCCESSFUL_USER_AUTH_DTO))
                .isInstanceOf(PasswordNotMatchException.class);
    }

    @Test
    @DisplayName("service : 유저 정보 수정 : 성공")
    void successfulModifyUser() {
        //given
        //when
        userService.modifyUser(SUCCESSFUL_USER_MODIFY_DTO, 1L);
        //then
        verify(userMapper).updateUser(any());
        verify(loginService).logout();
    }

    @Test
    @DisplayName("service : 유저 삭제 : 성공")
    void successfulWithdrawUser() {
        //given
        //when
        long userNo = 1L;
        userService.withdrawUser(userNo);
        //then
        verify(userMapper).delete(userNo);
    }

    @Test
    @DisplayName("service : 유저 정보 반환 : 성공")
    void successfulFindModifyUserDtoByUserNo() {
        //given
        given(userMapper.findByNo(any())).willReturn(SUCCESSFUL_USER_MODIFY);
        //when
        MyInfoDto modifyUserDtoByUserNo = userService.findModifyUserDtoByUserNo(1L);
        //then
        assertThat(DEFAULT_UPDATE_AFTER_MY_INFO).isEqualTo(modifyUserDtoByUserNo);
    }

    @Test
    @DisplayName("service : 유저 프로필 이미지 변경 : 현재 프로필 이미지가 없는 경우 성공 ")
    void successfulModifyWithoutUserProfile() {
        //given
        long userNo = 1L;
        given(userMapper.findUserProfileImageByNo(any())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        //when
        String result = userService.modifyUserProfile(MODIFY_USER_PROFILE_IMG, userNo);
        //then
        assertThat(result).isEqualTo(null);
        verify(userMapper).insertProfile(MODIFY_USER_PROFILE_IMG);
        verify(userMapper).updateUserProfile(MODIFY_USER_PROFILE_IMG.getImageNo(), userNo);
    }

    @Test
    @DisplayName("service : 유저 프로필 이미지 변경 : 현재 프로필 이미지가  있는 경우 성공")
    void successfulModifyUserProfile() {
        //given
        long userNo = 1L;
        given(userMapper.findUserProfileImageByNo(any())).willReturn(DEFAULT_USER);
        //when
        String result = userService.modifyUserProfile(MODIFY_USER_PROFILE_IMG, userNo);
        //then
        assertThat(result).isEqualTo("profile/724198a2-ef0e-4c97-9fe1-2324125125.jpg");
        verify(userMapper).insertProfile(MODIFY_USER_PROFILE_IMG);
        verify(userMapper).updateUserProfile(MODIFY_USER_PROFILE_IMG.getImageNo(), userNo);
        verify(userMapper).deleteProfileImage(DEFAULT_USER.getUserProfileImage().getImageNo());
    }


    @Test
    @DisplayName("service : 유저 비밀번호 변경 : 성공")
    void successfulChangePassword() {
        //given
        long userNo = 1L;
        given(userMapper.findByNo(any())).willReturn(DEFAULT_USER);
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(passwordEncoder.encode(any())).willReturn("encodePassword");
        //when
        userService.changePassword(DEFAULT_CHANGE_PASSWORD_DTO, userNo);
        //then
        verify(userMapper).updateUserPassword(userNo, "encodePassword");
    }


    @Test
    @DisplayName("service : 유저 비밀번호 변경 : 실패")
    void failChangePassword() {
        //given
        given(userMapper.findByNo(any())).willReturn(DEFAULT_USER);
        given(passwordEncoder.matches(any(), any())).willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> userService.changePassword(DEFAULT_CHANGE_PASSWORD_DTO, 1L))
                .isInstanceOf(PasswordNotMatchException.class);
    }

}