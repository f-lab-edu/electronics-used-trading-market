package kr.flab.tradingmarket.domain.user.config;

import java.time.LocalDate;

import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;

public class UserCommonFixture {

    public static final String SESSION_NAME = "USER_NO";
    public static final UserProfileImage DEFAULT_USER_PROFILE_IMG = UserProfileImage.builder()
        .imageNo(1L)
        .originalFileName("DefaultImage.jpg")
        .fileLink("https://image1.kr.object.ncloudstorage.com/profile/724198a2-ef0e-4c97-9fe1-2324125125.jpg")
        .fileSize(0L)
        .build();
    public static final UserProfileImage UPDATE_USER_PROFILE_IMG = UserProfileImage.builder()
        .imageNo(2L)
        .originalFileName("UpdateTest.jpg")
        .fileLink("https://image1.kr.object.ncloudstorage.com/profile/2345666-ef0e-4c97-9fe1-22222333.jpg")
        .fileSize(0L)
        .build();
    public static final MockMultipartFile UPDATE_MOCK_PROFILE_IMAGE_FILE
        = new MockMultipartFile("image", "UpdateTest.jpg", MediaType.MULTIPART_FORM_DATA.toString(), "".getBytes());
    public static final User DEFAULT_USER = User.builder()
        .userNo(1L)
        .userId("testUser1")
        .userName("testUsername")
        .userPassword("testPassword1")
        .userPhone("01012341232")
        .userBirth(LocalDate.of(1997, 10, 30))
        .userProfileImage(DEFAULT_USER_PROFILE_IMG)
        .build();
    public static final ChangePasswordDto DEFAULT_CHANGE_PASSWORD_DTO = ChangePasswordDto.builder()
        .currentPassword("testPassword1")
        .password("testChangePw1")
        .confirmPassword("testChangePw1")
        .build();
    public static final ModifyUserDto DEFAULT_USER_MODIFY_DTO = ModifyUserDto.builder()
        .userName("testUserName2")
        .userPhone("01012341111")
        .build();
    public static final UserAuthDto DEFAULT_LOGIN_USER = UserAuthDto.builder()
        .userId("testUser1")
        .userPassword("testPassword1")
        .build();
    public static final JoinUserDto DEFAULT_JOIN_USER_DTO = JoinUserDto.builder()
        .userId("testUser1")
        .userName("testUsername")
        .userPassword("testPassword1")
        .userPhone("01012341232")
        .userBirth(LocalDate.of(1997, 10, 30))
        .build();

    public static final ArgumentCaptor<JoinUserDto> JOIN_USER_DTO_CAPTURE = ArgumentCaptor.forClass(JoinUserDto.class);
    public static final ArgumentCaptor<UserAuthDto> USER_AUTH_DTO_CAPTURE = ArgumentCaptor.forClass(UserAuthDto.class);
    public static final ArgumentCaptor<Long> LONG_CAPTURE = ArgumentCaptor.forClass(Long.class);
    public static final ArgumentCaptor<ModifyUserDto> MODIFY_USER_DTO_CAPTURE =
        ArgumentCaptor.forClass(ModifyUserDto.class);
    public static final ArgumentCaptor<MultipartFile> MULTIPART_FILE_CAPTURE =
        ArgumentCaptor.forClass(MultipartFile.class);
    public static final ArgumentCaptor<ImageType> IMAGE_TYPE_CAPTURE =
        ArgumentCaptor.forClass(ImageType.class);
    public static final ArgumentCaptor<UserProfileImage> USER_PROFILE_IMAGE_CAPTURE =
        ArgumentCaptor.forClass(UserProfileImage.class);
    public static final ArgumentCaptor<String> STRING_CAPTURE = ArgumentCaptor.forClass(String.class);
    public static final ArgumentCaptor<ChangePasswordDto> CHANGE_PASSWORD_DTO_CAPTURE = ArgumentCaptor.forClass(
        ChangePasswordDto.class);

}
