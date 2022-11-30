package kr.flab.tradingmarket.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.flab.tradingmarket.common.aop.LoginCheckAop;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
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
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static kr.flab.tradingmarket.domain.user.controller.UserControllerTestFixture.SUCCESSFUL_JOIN_USER;
import static kr.flab.tradingmarket.domain.user.controller.UserControllerTestFixture.*;
import static kr.flab.tradingmarket.domain.user.service.UserServiceTestFixture.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@Import({AopAutoConfiguration.class, LoginCheckAop.class})
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


    @Test
    @DisplayName("controller : 내정보 조회 : 성공")
    void successfulMyInfo() throws Exception {

        given(userService.findModifyUserDtoByUserNo(any()))
                .willReturn(ModifyUserDto.from(DEFAULT_USER));
        given(loginService.getLoginUserId())
                .willReturn(1L);

        mockMvc.perform(get(MY_INFO_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ModifyUserDto.from(DEFAULT_USER))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("controller : 내정보 조회 : 권한 없음 실패")
    void failMyInfo() throws Exception {

        given(userService.findModifyUserDtoByUserNo(any()))
                .willReturn(ModifyUserDto.from(DEFAULT_USER));
        given(loginService.getLoginUserId())
                .willReturn(null);

        mockMvc.perform(get(MY_INFO_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("controller : 내정보 변경 : 성공")
    void successfulModifyUser() throws Exception {

        long userId = 1L;
        given(loginService.getLoginUserId())
                .willReturn(userId);

        mockMvc.perform(patch(MY_INFO_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(SUCCESSFUL_USER_MODIFY_DTO)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).modifyUser(SUCCESSFUL_USER_MODIFY_DTO, userId);
    }

    @Test
    @DisplayName("controller : 내정보 변경 : 권한 없음 실패")
    void failModifyUser() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(null);

        mockMvc.perform(patch(MY_INFO_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(SUCCESSFUL_USER_MODIFY_DTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("controller : 회원탈퇴 : 성공")
    void successfulWithdrawUser() throws Exception {

        long userId = 1L;
        given(loginService.getLoginUserId())
                .willReturn(userId);

        mockMvc.perform(delete(WITHDRAW_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).withdrawUser(userId);
        verify(loginService).logout();
    }


    @Test
    @DisplayName("controller : 회원탈퇴 : 권한 없음 실패")
    void failWithdrawUser() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(null);

        mockMvc.perform(delete(WITHDRAW_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }


    @Test
    @DisplayName("controller : 회원 프로필 사진 변경 : 현재 프로필이 없을때 성공")
    void successfulModifyWithoutProfileImage() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(1L);
        given(userService.modifyUserProfile(any(), any()))
                .willReturn(null);

        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                        .file(MOCK_PROFILE_IMAGE_FILE))
                .andDo(print())
                .andExpect(status().isOk());

        verify(imageService).uploadImage(MOCK_PROFILE_IMAGE_FILE, ImageType.PROFILE);
        verify(imageService, never()).deleteImage(any());

    }

    @Test
    @DisplayName("controller : 회원 프로필 사진 변경 : 현재 프로필이 있을때 성공")
    void successfulModifyProfileImage() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(1L);
        given(imageService.uploadImage(any(), any()))
                .willReturn(DEFAULT_USER_PROFILE_IMG.getFileLink());
        given(userService.modifyUserProfile(any(), any()))
                .willReturn("testRemoveFileName");

        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                        .file(MOCK_PROFILE_IMAGE_FILE))
                .andDo(print())
                .andExpect(status().isOk());

        verify(imageService).uploadImage(MOCK_PROFILE_IMAGE_FILE, ImageType.PROFILE);
        verify(imageService).deleteImage(any());

    }


    @Test
    @DisplayName("controller : 회원 프로필 사진 변경 : DB 트랜잭션 실패로 인한 실패")
    void failModifyProfileImageCausedByTransaction() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(1L);
        given(imageService.uploadImage(any(), any()))
                .willReturn(DEFAULT_USER_PROFILE_IMG.getFileLink());
        given(userService.modifyUserProfile(any(), any()))
                .willThrow(ImageUploadException.class);

        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                        .file(MOCK_PROFILE_IMAGE_FILE))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(imageService).deleteImage(any());
    }


    @Test
    @DisplayName("controller : 회원 프로필 사진 변경 : 권한 없음 실패")
    void failModifyProfileImageCausedByAccessDenied() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(null);

        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                        .file(MOCK_PROFILE_IMAGE_FILE))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(imageService, never()).uploadImage(any(), any());
    }

    @Test
    @DisplayName("controller : 회원 프로필 사진 변경 : IOException 관련 예외로 인한 실패")
    void failModifyProfileImageCausedByIOException() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(1L);
        given(imageService.uploadImage(any(), any()))
                .willThrow(ImageUploadException.class);

        mockMvc.perform(multipart(HttpMethod.PUT, MODIFY_PROFILE_IMAGE_URL)
                        .file(MOCK_PROFILE_IMAGE_FILE))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(userService, never()).modifyUserProfile(any(), any());
    }


    @Test
    @DisplayName("controller : 회원 비밀번호 변경 : 성공")
    void successfulChangePassword() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(1L);

        mockMvc.perform(patch(MODIFY_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(DEFAULT_CHANGE_PASSWORD_DTO))
                )
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).changePassword(any(), any());
    }

    @Test
    @DisplayName("controller : 회원 비밀번호 변경 : 권한 없음 실패")
    void failChangePasswordCausedByAccessDenied() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(null);

        mockMvc.perform(patch(MODIFY_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(DEFAULT_CHANGE_PASSWORD_DTO))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(userService, never()).changePassword(any(), any());
    }


    @Test
    @DisplayName("controller : 회원 비밀번호 변경 : 현재 비밀번호가 틀려서 실패")
    void failChangePasswordCausedByPasswordNotMatch() throws Exception {

        given(loginService.getLoginUserId())
                .willReturn(1L);
        doThrow(PasswordNotMatchException.class)
                .when(userService)
                .changePassword(any(), any());


        mockMvc.perform(patch(MODIFY_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(DEFAULT_CHANGE_PASSWORD_DTO))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService).changePassword(any(), any());
    }


}