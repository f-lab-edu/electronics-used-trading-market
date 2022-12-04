package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import kr.flab.tradingmarket.utils.ThreadExceptionTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static kr.flab.tradingmarket.domain.user.service.UserServiceTestFixture.SUCCESSFUL_JOIN_USER_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@MybatisTest
@ActiveProfiles("test")
class UserIdDuplicateTest {

    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    LoginService loginService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("delete from users");
        userService = new DefaultUserService(userMapper, passwordEncoder, loginService);
        given(passwordEncoder.encode(any())).willReturn("encodePassword");
    }


    @Test
    @DisplayName("serivce : 트랜젝션 테스트 : 동시성이슈 테스트 UserIdDuplicateException 발생 (isDuplicateUserId 동작 실패)")
    void concurrencyJoinTransaction() {
        assertThatThrownBy(() -> {
            ThreadExceptionTester t1 = new ThreadExceptionTester(() -> userService.joinUser(SUCCESSFUL_JOIN_USER_DTO));
            ThreadExceptionTester t2 = new ThreadExceptionTester(() -> userService.joinUser(SUCCESSFUL_JOIN_USER_DTO));
            t1.start();
            t2.start();
            t1.test();
            t2.test();
        }).isInstanceOf(UserIdDuplicateException.class);

        String findUserId = SUCCESSFUL_JOIN_USER_DTO.getUserId();
        int countUser = userMapper.countById(findUserId);
        User findUser = userMapper.findById(findUserId);

        assertThat(findUser.getUserId()).isEqualTo(findUserId);
        assertThat(countUser).isEqualTo(1);
    }

    @Test
    @DisplayName("serivce : 트랜젝션 테스트 : 회원가입시 아이디가 중복되는경우 isDuplicateUserId 동작")
    void joinTransaction() {
        userService.joinUser(SUCCESSFUL_JOIN_USER_DTO);

        assertThatThrownBy(() -> userService.joinUser(SUCCESSFUL_JOIN_USER_DTO)).isInstanceOf(UserIdDuplicateException.class);
        String findUserId = SUCCESSFUL_JOIN_USER_DTO.getUserId();
        int countUser = userMapper.countById(findUserId);
        User findUser = userMapper.findById(findUserId);

        assertThat(findUser.getUserId()).isEqualTo(findUserId);
        assertThat(countUser).isEqualTo(1);
        userMapper.delete(findUser.getUserNo());
    }

}

