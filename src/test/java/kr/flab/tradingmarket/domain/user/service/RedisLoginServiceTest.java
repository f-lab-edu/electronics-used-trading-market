package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import static kr.flab.tradingmarket.domain.user.controller.UserControllerTestFixture.SUCCESSFUL_LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RedisLoginServiceTest {


    RedisLoginService loginService;
    @Mock
    UserService userService;
    MockHttpSession mockSession;

    @BeforeEach
    void setUp() {
        mockSession = new MockHttpSession();
        loginService = new RedisLoginService(userService, mockSession);
    }

    @Test
    @DisplayName("serivce : 로그인 테스트 : 성공")
    void successfulLogin() {
        given(userService.userAuthCheck(SUCCESSFUL_LOGIN_USER))
                .willReturn(1L);

        loginService.login(SUCCESSFUL_LOGIN_USER);

        assertThat(mockSession.getAttribute("USER_ID")).isEqualTo(1L);

    }

    @Test
    @DisplayName("serivce : 로그인 테스트 : 비밀번호 오류로 인한 실패")
    void failLoginByPasswordNotMatch() {
        given(userService.userAuthCheck(SUCCESSFUL_LOGIN_USER))
                .willThrow(PasswordNotMatchException.class);


        assertThatThrownBy(() -> loginService.login(SUCCESSFUL_LOGIN_USER))
                .isInstanceOf(PasswordNotMatchException.class);


        assertThat(mockSession.getAttribute("USER_ID")).isNull();
    }

    @Test
    @DisplayName("serivce : 로그인 테스트 : 존재하지 않는 UserId 실패")
    void failLoginByUserNotFound() {
        given(userService.userAuthCheck(SUCCESSFUL_LOGIN_USER))
                .willThrow(UserNotFoundException.class);

        assertThatThrownBy(() -> loginService.login(SUCCESSFUL_LOGIN_USER))
                .isInstanceOf(UserNotFoundException.class);

        assertThat(mockSession.getAttribute("USER_ID")).isNull();
    }


}
