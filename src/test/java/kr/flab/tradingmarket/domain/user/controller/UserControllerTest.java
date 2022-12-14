package kr.flab.tradingmarket.domain.user.controller;

import static kr.flab.tradingmarket.domain.image.utils.ImageUtils.*;
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
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.service.LoginService;
import kr.flab.tradingmarket.domain.user.service.UserService;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@Import({AopAutoConfiguration.class, LoginCheckAop.class})
@ActiveProfiles("test")
class UserControllerTest {

    @MockBean
    UserService userService;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    LoginService loginService;
    @MockBean
    ImageService imageService;
    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    @DisplayName("controller : ???????????? ????????? : ??????")
    void successfulJoinUser() throws Exception {
        //given
        //when
        mockMvc.perform(post(JOIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(DEFAULT_JOIN_USER_DTO)))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(userService)
            .should()
            .joinUser(JOIN_USER_DTO_CAPTURE.capture());

        assertThat(JOIN_USER_DTO_CAPTURE.getValue()).isEqualTo(DEFAULT_JOIN_USER_DTO);
    }

    @Test
    @DisplayName("controller : ???????????? ????????? : ?????????????????? ?????? ??????")
    void failJoinUserByDuplication() throws Exception {
        //given
        willThrow(UserIdDuplicateException.class)
            .given(userService)
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
    @DisplayName("controller : ???????????? ????????? : birth ?????? ?????? ?????? ????????? ?????? ??????")
    void validationBirth() throws Exception {
        //given
        //when
        mockMvc.perform(post(JOIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FAIL_VALIDATION_BIRTH_FORMAT))
            .andDo(print())
            .andExpect(status().isBadRequest());

        //then
        then(userService)
            .should(never())
            .joinUser(any());

    }

    @Test
    @DisplayName("controller : ????????? ????????? : ??????")
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
        assertThat(USER_AUTH_DTO_CAPTURE.getValue()).isEqualTo(DEFAULT_LOGIN_USER);
    }

    @Test
    @DisplayName("controller : ????????? ????????? : ???????????? ????????? ?????? ??????")
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
    @DisplayName("controller : ????????? ????????? : ???????????? ?????? UserId ??????")
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
    @DisplayName("controller : ???????????? ????????? : ??????")
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
    @DisplayName("controller : ????????? ?????? : ??????")
    void successfulMyInfo() throws Exception {
        //given
        Long userNo = givenLogin();

        //when
        mockMvc.perform(get(MY_INFO_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(userService)
            .should()
            .findModifyUserDtoByUserNo(LONG_CAPTURE.capture());
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("controller : ????????? ?????? : ?????? ?????? ??????")
    void failMyInfo() throws Exception {
        //given
        givenNotLogin();

        //when
        mockMvc.perform(get(MY_INFO_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(userService).should(never())
            .findModifyUserDtoByUserNo(any());
    }

    @Test
    @DisplayName("controller : ????????? ?????? : ??????")
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
        then(userService)
            .should()
            .modifyUser(MODIFY_USER_DTO_CAPTURE.capture(), LONG_CAPTURE.capture());
        assertThat(MODIFY_USER_DTO_CAPTURE.getValue()).isEqualTo(DEFAULT_USER_MODIFY_DTO);
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("controller : ????????? ?????? : ?????? ?????? ??????")
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
        then(userService)
            .should(never())
            .modifyUser(any(), any());
    }

    @Test
    @DisplayName("controller : ???????????? : ??????")
    void successfulWithdrawUser() throws Exception {
        //given
        long userNo = givenLogin();

        //when
        mockMvc.perform(delete(WITHDRAW_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(userService)
            .should()
            .withdrawUser(LONG_CAPTURE.capture());
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("controller : ???????????? : ?????? ?????? ??????")
    void failWithdrawUser() throws Exception {
        //given
        givenNotLogin();

        //when
        mockMvc.perform(delete(WITHDRAW_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(userService)
            .should(never())
            .withdrawUser(any());
    }

    @Test
    @DisplayName("controller : ?????? ????????? ?????? ?????? : ?????? ???????????? ????????? ??????")
    void successfulModifyWithoutProfileImage() throws Exception {
        //given
        Long userNo = givenLogin();
        given(imageService.uploadImage(any(), any()))
            .willReturn(UPDATE_USER_PROFILE_IMG.getFileLink());

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                .file(UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(imageService)
            .should()
            .uploadImage(MULTIPART_FILE_CAPTURE.capture(), IMAGE_TYPE_CAPTURE.capture());
        assertThat(MULTIPART_FILE_CAPTURE.getValue()).isEqualTo(UPDATE_MOCK_PROFILE_IMAGE_FILE);
        assertThat(IMAGE_TYPE_CAPTURE.getValue()).isEqualTo(ImageType.PROFILE);

        then(userService)
            .should()
            .modifyUserProfile(USER_PROFILE_IMAGE_CAPTURE.capture(), LONG_CAPTURE.capture());
        assertThat(USER_PROFILE_IMAGE_CAPTURE.getValue())
            .extracting(UserProfileImage::getFileLink,
                UserProfileImage::getFileSize,
                UserProfileImage::getOriginalFileName)
            .containsExactly(UPDATE_USER_PROFILE_IMG.getFileLink(),
                UPDATE_USER_PROFILE_IMG.getFileSize(),
                UPDATE_USER_PROFILE_IMG.getOriginalFileName());
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);

        then(imageService)
            .should(never()).deleteImage(any());
    }

    @Test
    @DisplayName("controller : ?????? ????????? ?????? ?????? : ?????? ???????????? ????????? ??????")
    void successfulModifyProfileImage() throws Exception {
        //given
        Long userNo = givenLogin();
        given(imageService.uploadImage(any(), any()))
            .willReturn(UPDATE_USER_PROFILE_IMG.getFileLink());
        given(userService.modifyUserProfile(any(), any()))
            .willReturn(separateImagePath(DEFAULT_USER_PROFILE_IMG.getFileLink()));

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                .file(UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(imageService)
            .should()
            .uploadImage(any(), any());

        then(userService)
            .should()
            .modifyUserProfile(any(), any());

        then(imageService)
            .should()
            .deleteImage(STRING_CAPTURE.capture());
        assertThat(STRING_CAPTURE.getValue()).isEqualTo(separateImagePath(DEFAULT_USER_PROFILE_IMG.getFileLink()));
    }

    @Test
    @DisplayName("controller : ?????? ????????? ?????? ?????? : DB ???????????? ????????? ?????? ??????")
    void failModifyProfileImageCausedByTransaction() throws Exception {
        //given
        givenLogin();
        given(imageService.uploadImage(any(), any()))
            .willReturn(UPDATE_USER_PROFILE_IMG.getFileLink());
        given(userService.modifyUserProfile(any(), any()))
            .willThrow(ImageUploadException.class);

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                .file(UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        //then
        then(imageService)
            .should()
            .uploadImage(any(), any());

        then(userService)
            .should()
            .modifyUserProfile(any(), any());

        then(imageService)
            .should()
            .deleteImage(STRING_CAPTURE.capture());
        assertThat(STRING_CAPTURE.getValue()).isEqualTo(separateImagePath(UPDATE_USER_PROFILE_IMG.getFileLink()));
    }

    @Test
    @DisplayName("controller : ?????? ????????? ?????? ?????? : ?????? ?????? ??????")
    void failModifyProfileImageCausedByAccessDenied() throws Exception {
        //given
        givenNotLogin();

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                .file(UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(imageService)
            .should(never())
            .uploadImage(any(), any());
    }

    @Test
    @DisplayName("controller : ?????? ????????? ?????? ?????? : IOException ?????? ????????? ?????? ??????")
    void failModifyProfileImageCausedByIOException() throws Exception {
        //given
        givenLogin();
        given(imageService.uploadImage(any(), any()))
            .willThrow(ImageUploadException.class);

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                .file(UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        //then
        then(userService)
            .should(never())
            .modifyUserProfile(any(), any());
    }

    @Test
    @DisplayName("controller : ?????? ???????????? ?????? : ??????")
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
        then(userService)
            .should()
            .changePassword(CHANGE_PASSWORD_DTO_CAPTURE.capture(), LONG_CAPTURE.capture());
        assertThat(CHANGE_PASSWORD_DTO_CAPTURE.getValue()).isEqualTo(DEFAULT_CHANGE_PASSWORD_DTO);
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(userNo);
    }

    @Test
    @DisplayName("controller : ?????? ???????????? ?????? : ?????? ?????? ??????")
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
        then(userService)
            .should(never())
            .changePassword(any(), any());
    }

    @Test
    @DisplayName("controller : ?????? ???????????? ?????? : ?????? ??????????????? ????????? ??????")
    void failChangePasswordCausedByPasswordNotMatch() throws Exception {
        //given
        givenLogin();
        willThrow(PasswordNotMatchException.class)
            .given(userService)
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