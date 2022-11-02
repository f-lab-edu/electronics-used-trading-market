package kr.flab.tradingmarket.domain.user.mapper;

import kr.flab.tradingmarket.domain.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName :  kr.flab.tradingmarket.domain.user.mapper
 * fileName : UserMapperTest
 * author :  ddh96
 * date : 2022-11-02
 * description :
 *
 * @MybatisTest를 사용한 UserMapper Test
 * @AutoConfigureTestDatabase -> 메모리 DB를 사용하지 않기위해서 설정
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-11-02                ddh96             최초 생성
 */

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {
    private static User user1;
    @Autowired
    UserMapper userMapper;

    @BeforeAll
    static void setupFixture() {
        user1 = User.builder()
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

    private void checkUserInfo(User findUser) {
        assertThat(user1.getUserId()).isEqualTo(findUser.getUserId());
        assertThat(user1.getUserName()).isEqualTo(findUser.getUserName());
        assertThat(user1.getUserPassword()).isEqualTo(findUser.getUserPassword());
        assertThat(user1.getUserPhone()).isEqualTo(findUser.getUserPhone());
    }

    @Test
    @DisplayName("Mybatis Test user insert 및 userID/userNO select 테스트")
    void insertUserAndFind() {
        //given
        //when
        userMapper.insertUser(user1);
        User findUser = userMapper.findById(user1.getUserId());
        User findUser2 = userMapper.findByNo(user1.getUserNo());
        //then
        checkUserInfo(findUser);
        checkUserInfo(findUser2);
    }


}