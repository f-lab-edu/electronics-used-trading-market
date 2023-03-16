package kr.flab.tradingmarket.domain.user.service;

import static kr.flab.tradingmarket.domain.user.config.UserCommonFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;

@ExtendWith(MockitoExtension.class)
class DefaultUserCommandTest {

    @InjectMocks
    DefaultUserCommandService userCommandService;
    @Mock
    UserService userService;
    @Mock
    ImageService imageService;
    @Mock
    LoginService loginService;

    @Test
    @DisplayName("service : 회원가입 테스트 : 성공")
    void successfulJoinUser() throws Exception {
        //given
        //when
        userCommandService.joinUser(DEFAULT_JOIN_USER_DTO);

        //then
        then(userService)
            .should()
            .joinUser(any());
    }

    @Test
    @DisplayName("service : 회원가입 테스트 : 유저 중복으로 인한 실패")
    void failJoinUserByDuplication() throws Exception {
        //given
        willThrow(UserIdDuplicateException.class)
            .given(userService)
            .joinUser(any());

        //when
        //then
        assertThatThrownBy(() -> userCommandService.joinUser(DEFAULT_JOIN_USER_DTO))
            .isInstanceOf(UserIdDuplicateException.class);
    }

    @Test
    @DisplayName("service : 내정보 조회 : 성공")
    void successfulMyInfo() throws Exception {
        //given
        //when
        userCommandService.findModifyUserDtoByUserNo(1L);

        //then
        then(userService)
            .should()
            .findModifyUserDtoByUserNo(any());
    }

    @Test
    @DisplayName("controller : 내정보 변경 : 성공")
    void successfulModifyUser() throws Exception {
        //given
        //then
        userCommandService.modifyUser(DEFAULT_USER_MODIFY_DTO, 1L);

        //then
        then(userService)
            .should()
            .modifyUser(MODIFY_USER_DTO_CAPTURE.capture(), LONG_CAPTURE.capture());
        assertThat(MODIFY_USER_DTO_CAPTURE.getValue()).isEqualTo(DEFAULT_USER_MODIFY_DTO);
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("service : 회원 탈퇴 : 성공")
    void successfulWithdrawUser() throws Exception {
        //given
        //when
        userCommandService.withdrawUser(1L);

        //then
        then(userService)
            .should()
            .withdrawUser(any());
        then(loginService)
            .should()
            .logout();
    }

    @Test
    @DisplayName("service : 회원 프로필 사진 변경 : 현재 프로필이 없을때 성공")
    void successfulModifyWithoutProfileImage() throws Exception {
        //given
        given(imageService.uploadImage(any(), any()))
            .willReturn(UPDATE_USER_PROFILE_IMG.getFileLink());

        //when
        userCommandService.modifyUserProfile(1L, UPDATE_MOCK_PROFILE_IMAGE_FILE);

        //then
        then(imageService)
            .should()
            .uploadImage(any(), any());

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
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(1L);

        then(imageService)
            .should(never()).deleteImage(any());
    }

    @Test
    @DisplayName("service : 회원 프로필 사진 변경 : 현재 프로필이 있을때 성공")
    void successfulModifyProfileImage() throws Exception {
        //given
        given(imageService.uploadImage(any(), any()))
            .willReturn(UPDATE_USER_PROFILE_IMG.getFileLink());
        given(userService.modifyUserProfile(any(), any()))
            .willReturn(DEFAULT_USER_PROFILE_IMG);

        //when
        userCommandService.modifyUserProfile(1L, UPDATE_MOCK_PROFILE_IMAGE_FILE);

        //then
        then(imageService)
            .should()
            .uploadImage(any(), any());

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
        assertThat(LONG_CAPTURE.getValue()).isEqualTo(1L);

        then(imageService)
            .should()
            .deleteImage(any());
    }

    @Test
    @DisplayName("service : 회원 프로필 사진 변경 : DB 트랜잭션 실패로 인한 실패")
    void failModifyProfileImageCausedByTransaction() throws Exception {
        //given
        given(imageService.uploadImage(any(), any()))
            .willReturn(UPDATE_USER_PROFILE_IMG.getFileLink());
        given(userService.modifyUserProfile(any(), any()))
            .willThrow(ImageUploadException.class);

        //when
        assertThatThrownBy(() -> userCommandService.modifyUserProfile(1L, UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .isInstanceOf(ImageUploadException.class);

        //then
        then(imageService)
            .should()
            .uploadImage(any(), any());
        then(userService)
            .should()
            .modifyUserProfile(any(), any());
        then(imageService)
            .should()
            .deleteImage(any());
    }

    @Test
    @DisplayName("service : 회원 프로필 사진 변경 : IOException 관련 예외로 인한 실패")
    void failModifyProfileImageCausedByIOException() throws Exception {
        //given
        given(imageService.uploadImage(any(), any()))
            .willThrow(ImageUploadException.class);

        //when
        assertThatThrownBy(() -> userCommandService.modifyUserProfile(1L, UPDATE_MOCK_PROFILE_IMAGE_FILE))
            .isInstanceOf(ImageUploadException.class);

        //then
        then(userService)
            .should(never())
            .modifyUserProfile(any(), any());
    }

    @Test
    @DisplayName("service : 회원 비밀번호 변경 : 성공")
    void successfulChangePassword() throws Exception {
        //given
        //when
        userCommandService.changePassword(DEFAULT_CHANGE_PASSWORD_DTO, 1L);

        //then
        then(userService)
            .should()
            .changePassword(any(), any());

    }

    @Test
    @DisplayName("service : 회원 비밀번호 변경 : 현재 비밀번호가 틀려서 실패")
    void failChangePasswordCausedByPasswordNotMatch() throws Exception {
        //given
        willThrow(PasswordNotMatchException.class)
            .given(userService)
            .changePassword(any(), any());

        //when
        //then
        assertThatThrownBy(() -> userCommandService.changePassword(any(), any()))
            .isInstanceOf(PasswordNotMatchException.class);
    }

}
