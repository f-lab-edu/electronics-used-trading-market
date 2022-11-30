package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.dto.response.MyInfoDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserServiceTestFixture {
    public final static User SUCCESSFUL_JOIN_USER =
            User.builder()
                    .userId("testUser1")
                    .userName("testUsername")
                    .userPassword("testPassword1")
                    .userPhone("01012341234")
                    .userBirth(LocalDate.now())
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
    public final static JoinUserDto SUCCESSFUL_JOIN_USER_DTO = JoinUserDto.builder()
            .userId("testUser1")
            .userName("testUsername")
            .userPassword("testPassword1")
            .userPhone("01012341234")
            .userBirth(LocalDate.now())
            .build();
    public final static User SUCCESSFUL_JOIN_RETURN_USER = User.builder()
            .userNo(1L)
            .userId("testUser1")
            .userName("testUsername")
            .userPassword("testPassword1")
            .userPhone("01012341234")
            .userBirth(LocalDate.now())
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
            .build();
    public final static UserAuthDto SUCCESSFUL_USER_AUTH_DTO = UserAuthDto.builder()
            .userId("testUser1")
            .userPassword("testPassword1")
            .build();
    public final static ModifyUserDto SUCCESSFUL_USER_MODIFY_DTO = ModifyUserDto.builder()
            .userName("testUserName2")
            .userPhone("01012341111")
            .build();
    public final static User SUCCESSFUL_USER_MODIFY = User.builder()
            .userNo(1L)
            .userName("testUserName2")
            .userPhone("01012341111")
            .build();
    public final static UserProfileImage MODIFY_USER_PROFILE_IMG = UserProfileImage.builder()
            .imageNo(2L)
            .originalFileName("test2.jpg")
            .fileLink("https://image1.kr.object.ncloudstorage.com/profile/1231555-44232-2222-d222-2324125125.jpg")
            .modifyDate(LocalDateTime.now())
            .createDate(LocalDateTime.now())
            .fileSize(1L)
            .build();


    public final static UserProfileImage DEFAULT_USER_PROFILE_IMG = UserProfileImage.builder()
            .imageNo(1L)
            .originalFileName("test.jpg")
            .fileLink("https://image1.kr.object.ncloudstorage.com/profile/724198a2-ef0e-4c97-9fe1-2324125125.jpg")
            .modifyDate(LocalDateTime.now())
            .createDate(LocalDateTime.now())
            .fileSize(1L)
            .build();

    public final static User DEFAULT_USER = User.builder()
            .userNo(1L)
            .userId("testUser1")
            .userName("testUsername")
            .userPassword("testPassword1")
            .userPhone("01012341234")
            .userBirth(LocalDate.now())
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
            .userProfileImage(DEFAULT_USER_PROFILE_IMG)
            .build();

    public final static ChangePasswordDto DEFAULT_CHANGE_PASSWORD_DTO = ChangePasswordDto.builder()
            .currentPassword("testPassword1")
            .password("testChangePw1")
            .confirmPassword("testChangePw1")
            .build();
    public final static MyInfoDto DEFAULT_UPDATE_AFTER_MY_INFO = MyInfoDto.builder()
            .userName("testUserName2")
            .userPhone("01012341111")
            .build();

}
