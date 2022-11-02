package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * packageName :  kr.flab.tradingmarket.domain.user.service
 * fileName : DefaultUserServiceTest
 * author :  ddh96
 * date : 2022-11-02
 * description :
 * <p>
 * DefaultUserService 테스트
 * Mapper는 Mock처리
 * <p>
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-11-02                ddh96             최초 생성
 */
@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {

    private static User before;
    private static User after;
    @InjectMocks
    DefaultUserService userService;
    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder encoder;

    @BeforeAll
    static void setupFixture() {
        before = User.builder()
                .userId("testId1")
                .userName("testUsername")
                .userPassword("testPassword1")
                .userPhone("01012341234")
                .userBirth(LocalDate.now())
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .userProfileImage(null)
                .build();
        after = User.builder()
                .userNo(1L)
                .userId("testId1")
                .userName("testUsername")
                .userPassword("testPassword1")
                .userPhone("01012341234")
                .userBirth(LocalDate.now())
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .userProfileImage(null)
                .build();
    }


    @Test
    @DisplayName("회원가입성공")
    void joinUserSuccess() {
        //given
        given(userMapper.findById(before.getUserId())).willReturn(null);
        given(userMapper.insertUser(before)).willReturn(1L);
        given(userMapper.findByNo(1L)).willReturn(after);
        //when
        Long userId = userService.joinUser(before);
        User findUser = userMapper.findByNo(userId);
        //then
        assertThat(userId).isEqualTo(1L);
        assertThat(findUser).isEqualTo(after);
    }

    @Test
    @DisplayName("중복아이디 회원가입 실패")
    void joinUserFail() {
        //given
        given(userMapper.findById(before.getUserId())).willReturn(before);
        //when
        //then
        assertThatThrownBy(() -> userService.joinUser(before)).isInstanceOf(UserIdDuplicateException.class);

    }

    @Test
    @DisplayName("중복된 사용자 있음")
    void isDuplicateUserIdTrue() {
        //given
        given(userMapper.findById(before.getUserId())).willReturn(before);
        //when
        boolean result = userService.isDuplicateUserId(before.getUserId());
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("중복된 사용자 없음")
    void isDuplicateUserIdFalse() {
        //given
        given(userMapper.findById(before.getUserId())).willReturn(null);
        //when
        boolean result = userService.isDuplicateUserId(before.getUserId());
        //then
        assertThat(result).isFalse();
    }
}