package kr.flab.tradingmarket.domain.user.controller;

import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;

import java.time.LocalDate;

public class UserControllerTestFixture {
    public static final String JOIN_URL = "/join";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";


    public static final JoinUserDto SUCCESSFUL_JOIN_USER = JoinUserDto.builder()
            .userId("testId123")
            .userName("홍길동")
            .userPassword("testPassword1")
            .userPhone("01012341232")
            .userBirth(LocalDate.parse("1997-10-30"))
            .build();
    public static final JoinUserDto FAIL_VALIDATION_SHORT_ID = JoinUserDto.builder()
            .userId("aa")
            .userName("홍길동")
            .userPassword("testPassword1")
            .userPhone("01012341232")
            .userBirth(LocalDate.parse("1997-10-30"))
            .build();
    public static final String FAIL_VALIDATION_BIRTH_FORMAT = "{\n" +
            "    \"userId\" : \"akkkk222\",\n" +
            "    \"userName\":\"akkkk\",\n" +
            "    \"userPassword\":\"testPass123\",\n" +
            "    \"userPhone\" : \"01012341234\",\n" +
            "    \"userBirth\" : \"1997-1030\"\n" +
            "}";
    public static final JoinUserDto FAIL_VALIDATION_BIRTH_NULL = JoinUserDto.builder()
            .userId("testId123")
            .userName("홍길동")
            .userPassword("testPassword1")
            .userPhone("01012341232")
            .build();
    public static final JoinUserDto FAIL_VALIDATION_LONG_USERNAME = JoinUserDto.builder()
            .userId("testId123")
            .userName("12345678912345678900")
            .userPassword("testPassword1")
            .userPhone("01012341232")
            .userBirth(LocalDate.parse("1997-10-30"))
            .build();
    public static final JoinUserDto FAIL_VALIDATION_WRONG_PATTERN_PASSWORD = JoinUserDto.builder()
            .userId("testId123")
            .userName("홍길동")
            .userPassword("ddssaasdds")
            .userPhone("01012341232")
            .userBirth(LocalDate.parse("1997-10-30"))
            .build();

    public static final JoinUserDto FAIL_VALIDATION_WRONG_PATTERN_PHONE = JoinUserDto.builder()
            .userId("testId123")
            .userName("홍길동")
            .userPassword("testPassword1")
            .userPhone("ssssss1")
            .userBirth(LocalDate.parse("1997-10-30"))
            .build();


    public static final UserAuthDto SUCCESSFUL_LOGIN_USER = UserAuthDto.builder()
            .userId("successUser1")
            .userPassword("successPassword1")
            .build();

    public static final UserAuthDto FAIL_LOGIN_USER = UserAuthDto.builder()
            .userId("failUser1")
            .userPassword("failPassword1")
            .build();

}
