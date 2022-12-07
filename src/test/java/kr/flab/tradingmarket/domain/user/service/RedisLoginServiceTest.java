package kr.flab.tradingmarket.domain.user.service;

import static kr.flab.tradingmarket.domain.user.config.UserCommonFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
class RedisLoginServiceTest {

    RedisLoginService loginService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserMapper userMapper;
    MockHttpSession mockSession;

    @BeforeEach
    void setUp() {
        mockSession = new MockHttpSession();
        loginService = new RedisLoginService(mockSession, passwordEncoder, userMapper);
    }

    @Test
    @DisplayName("serivce : 로그인 테스트 : 성공")
    void successfulLogin() {
        //given
        given(userMapper.findById(any())).willReturn(DEFAULT_USER);
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        //when
        loginService.login(DEFAULT_LOGIN_USER);
        //then
        assertThat(mockSession.getAttribute(SESSION_NAME)).isEqualTo(1L);
    }

    @Test
    @DisplayName("serivce : 로그인 테스트 : 비밀번호 오류로 인한 실패")
    void failLoginByPasswordNotMatch() {
        //given
        given(userMapper.findById(any())).willReturn(DEFAULT_USER);
        given(passwordEncoder.matches(any(), any())).willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> loginService.login(DEFAULT_LOGIN_USER))
            .isInstanceOf(PasswordNotMatchException.class);
        assertThat(mockSession.getAttribute(SESSION_NAME)).isNull();
    }

    @Test
    @DisplayName("serivce : 로그인 테스트 : 존재하지 않는 UserId 실패")
    void failLoginByUserNotFound() {
        //given
        given(userMapper.findById(any())).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> loginService.login(DEFAULT_LOGIN_USER))
            .isInstanceOf(UserNotFoundException.class);
        verify(passwordEncoder, never()).matches(any(), any());
        assertThat(mockSession.getAttribute(SESSION_NAME)).isNull();
    }

}
