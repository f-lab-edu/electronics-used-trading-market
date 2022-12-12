package kr.flab.tradingmarket.domain.user.controller;

import java.time.LocalDate;

import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;

public class UserControllerTestFixture {
    public static final String JOIN_URL = "/join";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String MY_INFO_URL = "/my/info";
    public static final String WITHDRAW_URL = "/my/withdraw";
    public static final String MODIFY_PROFILE_IMAGE_URL = "/my/profile-image";
    public static final String MODIFY_PASSWORD_URL = "/my/password";
    public static final String FAIL_VALIDATION_BIRTH_FORMAT = "{\n" +
        "    \"userId\" : \"testUser1\",\n" +
        "    \"userName\":\"testPassword1\",\n" +
        "    \"userPassword\":\"testPassword1\",\n" +
        "    \"userPhone\" : \"01012341232\",\n" +
        "    \"userBirth\" : \"1997-1030\"\n" +
        "}";
    public static final JoinUserDto FAIL_VALIDATION_WRONG_PATTERN_PASSWORD = JoinUserDto.builder()
        .userId("testUser1")
        .userName("testUsername")
        .userPassword("failPassword")
        .userPhone("01012341232")
        .userBirth(LocalDate.parse("1997-10-30"))
        .build();
    public static final JoinUserDto FAIL_VALIDATION_BLANK_PASSWORD = JoinUserDto.builder()
        .userId("testUser1")
        .userName("testUsername")
        .userPhone("01012341232")
        .userBirth(LocalDate.parse("1997-10-30"))
        .build();
    public static final JoinUserDto FAIL_VALIDATION_WRONG_PATTERN_PHONE = JoinUserDto.builder()
        .userId("testUser1")
        .userName("testUsername")
        .userPassword("testPassword1")
        .userPhone("1")
        .userBirth(LocalDate.parse("1997-10-30"))
        .build();
    public static final JoinUserDto FAIL_VALIDATION_BLANK_PHONE = JoinUserDto.builder()
        .userId("testUser1")
        .userName("testUsername")
        .userPassword("testPassword1")
        .userBirth(LocalDate.parse("1997-10-30"))
        .build();
    public static final ChangePasswordDto FAIL_VALIDATION_NON_EQUALS_PASSWORD = ChangePasswordDto.builder()
        .currentPassword("testPassword1")
        .password("testChangePw1")
        .confirmPassword("testChangePw2")
        .build();
}
