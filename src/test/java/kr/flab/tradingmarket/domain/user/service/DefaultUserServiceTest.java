package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {


    @InjectMocks
    DefaultUserService userService;
    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("serivce : 회원가입 테스트 : 성공")
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
    @DisplayName("serivce : 회원가입 테스트 : 아이디 중복으로 인한 회원가입 실패")
    void joinUserFail() {
        //given
        given(userMapper.findById(SUCCESSFUL_JOIN_USER.getUserId())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        //when
        //then
        assertThatThrownBy(() -> userService.joinUser(SUCCESSFUL_JOIN_USER_DTO)).isInstanceOf(UserIdDuplicateException.class);

    }

    @Test
    @DisplayName("serivce : id 중복확인 테스트 : 중복된사용자 있음")
    void isDuplicateUserIdTrue() {
        //given
        given(userMapper.findById(SUCCESSFUL_JOIN_USER.getUserId())).willReturn(SUCCESSFUL_JOIN_RETURN_USER);
        //when
        boolean result = userService.isDuplicateUserId(SUCCESSFUL_JOIN_USER_DTO.getUserId());
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("serivce : id 중복확인 테스트 : 중복된사용자 없음")
    void isDuplicateUserIdFalse() {
        //given
        given(userMapper.findById(SUCCESSFUL_JOIN_USER.getUserId())).willReturn(null);
        //when
        boolean result = userService.isDuplicateUserId(SUCCESSFUL_JOIN_USER_DTO.getUserId());
        //then
        assertThat(result).isFalse();
    }
}