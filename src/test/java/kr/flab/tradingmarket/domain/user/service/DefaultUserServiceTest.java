package kr.flab.tradingmarket.domain.user.service;

import static kr.flab.tradingmarket.domain.user.config.UserCommonFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.flab.tradingmarket.domain.user.dto.response.MyInfoDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;

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
    void successfulJoinUser() {
        //given
        given(userMapper.findById(any()))
            .willReturn(null);

        //when
        userService.joinUser(DEFAULT_JOIN_USER_DTO);

        //then
        then(userMapper)
            .should()
            .insertUser(USER_CAPTURE.capture());
        assertThat(USER_CAPTURE.getValue()).isEqualTo(User.from(DEFAULT_JOIN_USER_DTO));
    }

    @Test
    @DisplayName("service : 회원가입 테스트 : 아이디 중복으로 인한 회원가입 실패")
    void failJoinUser() {
        //given
        given(userMapper.findById(any()))
            .willReturn(DEFAULT_USER);

        //when
        //then
        assertThatThrownBy(() -> userService.joinUser(DEFAULT_JOIN_USER_DTO))
            .isInstanceOf(UserIdDuplicateException.class);
    }

    @Test
    @DisplayName("service : id 중복확인 테스트 : 중복된사용자 있음")
    void trueIsDuplicateUserId() {
        //given
        given(userMapper.findById(any()))
            .willReturn(DEFAULT_USER);

        //when
        boolean result = userService.isDuplicateUserId(DEFAULT_JOIN_USER_DTO.getUserId());

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("service : id 중복확인 테스트 : 중복된사용자 없음")
    void falseIsDuplicateUserId() {
        //given
        given(userMapper.findById(any()))
            .willReturn(null);

        //when
        boolean result = userService.isDuplicateUserId(DEFAULT_USER.getUserId());

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("service : 유저 번호로 유저 찾기 : 성공")
    void successfulFindByUserNo() {
        //given
        given(userMapper.findByNo(any()))
            .willReturn(DEFAULT_USER);

        //when
        User findUser = userService.findByUserNo(DEFAULT_USER.getUserNo());

        //then
        assertThat(findUser).isEqualTo(DEFAULT_USER);

    }

    @Test
    @DisplayName("service : 유저 번호 유저찾기 : 해당유저가 없을때 실패 ")
    void failFindByUserNo() {
        //given
        given(userMapper.findByNo(any()))
            .willReturn(null);

        //when
        //then
        assertThatThrownBy(() -> userService.findByUserNo(any()))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("service : 유저 정보 수정 : 성공")
    void successfulModifyUser() {
        //given
        //when
        userService.modifyUser(DEFAULT_USER_MODIFY_DTO, 1L);

        //then
        then(userMapper)
            .should()
            .updateUser(USER_CAPTURE.capture());
        assertThat(USER_CAPTURE.getValue()).isEqualTo(User.from(DEFAULT_JOIN_USER_DTO));
        then(loginService)
            .should()
            .logout();

    }

    @Test
    @DisplayName("service : 유저 삭제 : 성공")
    void successfulWithdrawUser() {
        //given
        //when
        userService.withdrawUser(DEFAULT_USER.getUserNo());

        //then
        then(userMapper)
            .should()
            .delete(any());
    }

    @Test
    @DisplayName("service : 유저 정보 반환 : 성공")
    void successfulFindModifyUserDtoByUserNo() {
        //given
        given(userMapper.findByNo(any()))
            .willReturn(DEFAULT_USER);

        //when
        MyInfoDto modifyUserDtoByUserNo = userService.findModifyUserDtoByUserNo(DEFAULT_USER.getUserNo());

        //then
        assertThat(MyInfoDto.from(DEFAULT_USER)).isEqualTo(modifyUserDtoByUserNo);
    }

    @Test
    @DisplayName("service : 유저 프로필 이미지 변경 : 현재 프로필 이미지가 없는 경우 성공 ")
    void successfulModifyWithoutUserProfile() {
        //given
        given(userMapper.findUserProfileImageByNo(any()))
            .willReturn(DEFAULT_NO_PROFILE_USER);

        //when
        UserProfileImage result = userService.modifyUserProfile(UPDATE_USER_PROFILE_IMG,
            DEFAULT_NO_PROFILE_USER.getUserNo());

        //then
        assertThat(result).isEqualTo(null);
        then(userMapper)
            .should()
            .insertProfile(any());
        then(userMapper)
            .should()
            .updateUserProfile(any(), any());
    }

    @Test
    @DisplayName("service : 유저 프로필 이미지 변경 : 현재 프로필 이미지 있는 경우 성공")
    void successfulModifyUserProfile() {
        //given
        given(userMapper.findUserProfileImageByNo(any()))
            .willReturn(DEFAULT_USER);

        //when
        UserProfileImage result = userService.modifyUserProfile(UPDATE_USER_PROFILE_IMG, DEFAULT_USER.getUserNo());

        //then
        assertThat(result).isEqualTo(DEFAULT_USER_PROFILE_IMG);

        then(userMapper)
            .should()
            .insertProfile(any());
        then(userMapper)
            .should()
            .updateUserProfile(any(), any());
        then(userMapper)
            .should()
            .deleteProfileImage(any());
    }

    @Test
    @DisplayName("service : 유저 비밀번호 변경 : 성공")
    void successfulChangePassword() {
        //given
        given(userMapper.findByNo(any()))
            .willReturn(DEFAULT_USER);
        given(passwordEncoder.matches(any(), any()))
            .willReturn(true);
        given(passwordEncoder.encode(any()))
            .willReturn("encodePassword");

        //when
        userService.changePassword(DEFAULT_CHANGE_PASSWORD_DTO, DEFAULT_USER.getUserNo());

        //then
        then(userMapper)
            .should()
            .updateUserPassword(any(), any());
    }

    @Test
    @DisplayName("service : 유저 비밀번호 변경 : 실패")
    void failChangePassword() {
        //given
        given(userMapper.findByNo(any()))
            .willReturn(DEFAULT_USER);
        given(passwordEncoder.matches(any(), any()))
            .willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> userService.changePassword(DEFAULT_CHANGE_PASSWORD_DTO, 1L))
            .isInstanceOf(PasswordNotMatchException.class);
    }

}