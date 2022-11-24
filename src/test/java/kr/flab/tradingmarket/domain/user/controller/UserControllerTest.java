package kr.flab.tradingmarket.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.service.LoginService;
import kr.flab.tradingmarket.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static kr.flab.tradingmarket.domain.user.controller.UserControllerTestFixture.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {


    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;


    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }


    @Test
    @DisplayName("controller : 회원가입 테스트 : 성공")
    void successfulJoinUser() throws Exception {
        when(userService.joinUser(SUCCESSFUL_JOIN_USER))
                .thenReturn(1L);
        mockMvc.perform(post(JOIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(SUCCESSFUL_JOIN_USER)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("controller : 회원가입 테스트 : 유저중복으로 인한 실패")
    void failJoinUserByDuplication() throws Exception {
        when(userService.joinUser(SUCCESSFUL_JOIN_USER))
                .thenThrow(UserIdDuplicateException.class);
        mockMvc.perform(post(JOIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(SUCCESSFUL_JOIN_USER)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("controller : 회원가입 테스트 : birth 생일 날짜 형식 오류로 인한 실패")
    void validationBirth() throws Exception {
        mockMvc.perform(post(JOIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FAIL_VALIDATION_BIRTH_FORMAT))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("controller : 로그인 테스트 : 성공")
    void successfulLogin() throws Exception {
        doNothing()
                .when(loginService)
                .login(SUCCESSFUL_LOGIN_USER);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(SUCCESSFUL_LOGIN_USER)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("controller : 로그인 테스트 : 비밀번호 오류로 인한 실패")
    void failLoginByPasswordNotMatch() throws Exception {
        doThrow(PasswordNotMatchException.class)
                .when(loginService)
                .login(FAIL_LOGIN_USER);

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(FAIL_LOGIN_USER)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("controller : 로그인 테스트 : 존재하지 않는 UserId 실패")
    void failLoginByUserNotFound() throws Exception {
        doThrow(UserNotFoundException.class)
                .when(loginService)
                .login(FAIL_LOGIN_USER);

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(FAIL_LOGIN_USER)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("controller : 로그아웃 테스트 : 성공")
    void successfulLogout() throws Exception {
        doNothing()
                .when(loginService)
                .logout();

        mockMvc.perform(post(LOGOUT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(FAIL_LOGIN_USER)))
                .andDo(print())
                .andExpect(status().isOk());
    }


}