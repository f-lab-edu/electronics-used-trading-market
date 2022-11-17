package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.entity.User;

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
                    .userProfileImage(null)
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
            .userProfileImage(null)
            .build();


}
