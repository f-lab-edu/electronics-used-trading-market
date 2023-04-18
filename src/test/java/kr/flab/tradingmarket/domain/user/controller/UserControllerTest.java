package kr.flab.tradingmarket.domain.user.controller;

import static kr.flab.tradingmarket.domain.user.config.UserCommonFixture.*;
import static kr.flab.tradingmarket.domain.user.controller.UserControllerTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.flab.tradingmarket.common.aop.LoginCheckAop;
import kr.flab.tradingmarket.common.interceptor.ProductAuthorizationInterceptor;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.product.service.ProductService;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.service.LoginService;
import kr.flab.tradingmarket.domain.user.service.UserFacadeService;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@Import({AopAutoConfiguration.class, LoginCheckAop.class, ProductAuthorizationInterceptor.class})
@ActiveProfiles("test")
class UserControllerTest {

    @MockBean
    UserFacadeService userFacadeService;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    LoginService loginService;
    @MockBean
    ProductService productService;
    @Autowired
    ObjectMapper objectMapper;

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    @DisplayName("controller : 회원가입 테스트 : 성공")
    void successfulJoinUser() throws Exception {
        //given
        //when
        mockMvc.perform(post(JOIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_JOIN_USER_DTO)))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(userFacadeService)
            .should()
            .joinUser(JOIN_USER_DTO_CAPTURE.capture());

        assertThat(JOIN_USER_DTO_CAPTURE.getValue()).usingRecursiveComparison().isEqualTo(DEFAULT_JOIN_USER_DTO);
    }

    @Test
    @DisplayName("controller : 회원가입 테스트 : 유저중복으로 인한 실패")
    void failJoinUserByDuplication() throws Exception {
        //given
        willThrow(UserIdDuplicateException.class)
            .given(userFacadeService)
            .joinUser(any());

        //when
        //then
        mockMvc.perform(post(JOIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_JOIN_USER_DTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("controller : 회원가입 테스트 : birth 생일 날짜 형식 오류로 인한 실패")
    void validationBirth() throws Exception {
        //given
        //when
        mockMvc.perform(post(JOIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FAIL_VALIDATION_BIRTH_FORMAT))
            .andDo(print())
            .andExpect(status().isBadRequest());

        //then
        then(userFacadeService)
            .should(never())
            .joinUser(any());

    }

    @Test
    @DisplayName("controller : 로그인 테스트 : 성공")
    void successfulLogin() throws Exception {
        //given
        willDoNothing()
            .given(loginService)
            .login(any());

        //when
        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_LOGIN_USER)))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(loginService)
            .should()
            .login(USER_AUTH_DTO_CAPTURE.capture());
        assertThat(USER_AUTH_DTO_CAPTURE.getValue()).usingRecursiveComparison().isEqualTo(DEFAULT_LOGIN_USER);
    }

    @Test
    @DisplayName("controller : 로그인 테스트 : 비밀번호 오류로 인한 실패")
    void failLoginByPasswordNotMatch() throws Exception {
        //given
        willThrow(PasswordNotMatchException.class)
            .given(loginService)
            .login(any());

        //when
        //then
        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_LOGIN_USER)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("controller : 로그인 테스트 : 존재하지 않는 UserId 실패")
    void failLoginByUserNotFound() throws Exception {
        //given
        willThrow(UserNotFoundException.class)
            .given(loginService)
            .login(any());

        //when
        //then
        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_LOGIN_USER)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("controller : 로그아웃 테스트 : 성공")
    void successfulLogout() throws Exception {
        //given
        willDoNothing()
            .given(loginService)
            .logout();

        //when
        mockMvc.perform(post(LOGOUT_URL))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(loginService)
            .should()
            .logout();
    }

    @Test
    @DisplayName("controller : 내정보 조회 : 성공")
    void successfulMyInfo() throws Exception {
        //given
        Long userNo = givenLogin();

        //when
        mockMvc.perform(get(MY_INFO_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(userFacadeService)
            .should()
            .findModifyUserDtoByUserNo(LONG_CAPTURE.capture());
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("controller : 내정보 조회 : 권한 없음 실패")
    void failMyInfo() throws Exception {
        //given
        givenNotLogin();

        //when
        mockMvc.perform(get(MY_INFO_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(userFacadeService).should(never())
            .findModifyUserDtoByUserNo(any());
    }

    @Test
    @DisplayName("controller : 내정보 변경 : 성공")
    void successfulModifyUser() throws Exception {
        //given
        long userNo = givenLogin();

        //then
        mockMvc.perform(
                patch(MY_INFO_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(DEFAULT_USER_MODIFY_DTO)))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(userFacadeService)
            .should()
            .modifyUser(MODIFY_USER_DTO_CAPTURE.capture(), LONG_CAPTURE.capture());
        assertThat(MODIFY_USER_DTO_CAPTURE.getValue()).usingRecursiveComparison().isEqualTo(DEFAULT_USER_MODIFY_DTO);
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("controller : 내정보 변경 : 권한 없음 실패")
    void failModifyUser() throws Exception {
        //given
        givenNotLogin();

        //when
        mockMvc.perform(
                patch(MY_INFO_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(DEFAULT_USER_MODIFY_DTO)))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(userFacadeService)
            .should(never())
            .modifyUser(any(), any());
    }

    @Test
    @DisplayName("controller : 회원탈퇴 : 성공")
    void successfulWithdrawUser() throws Exception {
        //given
        long userNo = givenLogin();

        //when
        mockMvc.perform(delete(WITHDRAW_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(userFacadeService)
            .should()
            .withdrawUser(LONG_CAPTURE.capture());
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("controller : 회원탈퇴 : 권한 없음 실패")
    void failWithdrawUser() throws Exception {
        //given
        givenNotLogin();

        //when
        mockMvc.perform(delete(WITHDRAW_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(userFacadeService)
            .should(never())
            .withdrawUser(any());
    }

    @Test
    @DisplayName("controller : 회원 프로필 사진 변경 : 현재 프로필이 없을때 성공")
    void successfulModifyWithoutProfileImage() throws Exception {
        //given
        givenLogin();

        //when
        //then
        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                .file(UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("controller : 회원 프로필 사진 변경 : ImageUploadException발생 실패")
    void failModifyProfileImageCausedByTransaction() throws Exception {
        //given
        givenLogin();
        willThrow(ImageUploadException.class)
            .given(userFacadeService)
            .modifyUserProfile(any(), any());

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                .file(UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        //then
        then(userFacadeService)
            .should()
            .modifyUserProfile(any(), any());
    }

    @Test
    @DisplayName("controller : 회원 프로필 사진 변경 : 권한 없음 실패")
    void failModifyProfileImageCausedByAccessDenied() throws Exception {
        //given
        givenNotLogin();

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                .file(UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(userFacadeService)
            .should(never())
            .modifyUserProfile(any(), any());
    }

    @Test
    @DisplayName("controller : 회원 비밀번호 변경 : 성공")
    void successfulChangePassword() throws Exception {
        //given
        Long userNo = givenLogin();

        //when
        mockMvc.perform(patch(MODIFY_PASSWORD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_CHANGE_PASSWORD_DTO)))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(userFacadeService)
            .should()
            .changePassword(CHANGE_PASSWORD_DTO_CAPTURE.capture(), LONG_CAPTURE.capture());
        assertThat(CHANGE_PASSWORD_DTO_CAPTURE.getValue()).usingRecursiveComparison()
            .isEqualTo(DEFAULT_CHANGE_PASSWORD_DTO);
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("controller : 회원 비밀번호 변경 : 권한 없음 실패")
    void failChangePasswordCausedByAccessDenied() throws Exception {
        //given
        givenNotLogin();

        //when
        mockMvc.perform(patch(MODIFY_PASSWORD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_CHANGE_PASSWORD_DTO)))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(userFacadeService)
            .should(never())
            .changePassword(any(), any());
    }

    @Test
    @DisplayName("controller : 회원 비밀번호 변경 : 현재 비밀번호가 틀려서 실패")
    void failChangePasswordCausedByPasswordNotMatch() throws Exception {
        //given
        givenLogin();
        willThrow(PasswordNotMatchException.class)
            .given(userFacadeService)
            .changePassword(any(), any());

        //when
        //then
        mockMvc.perform(patch(MODIFY_PASSWORD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_CHANGE_PASSWORD_DTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    private Long givenLogin() {
        Long userNo = 1L;
        given(loginService.getLoginUserNo())
            .willReturn(userNo);
        return userNo;
    }

    private void givenNotLogin() {
        given(loginService.getLoginUserNo())
            .willReturn(null);
    }
}