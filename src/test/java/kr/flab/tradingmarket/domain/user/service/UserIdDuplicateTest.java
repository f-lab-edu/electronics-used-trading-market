package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.image.config.AwsConfig;
import kr.flab.tradingmarket.domain.image.service.AwsImageService;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import kr.flab.tradingmarket.utils.ThreadExceptionTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;

import static kr.flab.tradingmarket.domain.user.service.UserServiceTestFixture.SUCCESSFUL_JOIN_USER_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Profile("test")
public class UserIdDuplicateTest {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @MockBean
    AwsConfig awsConfig;

    @MockBean
    AwsImageService AwsImageService;


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
        userMapper.delete(findUser.getUserNo());
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

