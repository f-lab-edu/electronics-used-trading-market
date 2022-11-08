package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     *
     * @param user
     * @return 생성된 user_no 리턴
     * @throws UserIdDuplicateException 아이디 중복시에 리턴
     */
    @Override
    public Long joinUser(User user) {
        if (!isDuplicateUserId(user.getUserId())) {
            user.setEncryptionPassword(passwordEncoder.encode(user.getUserPassword()));
            return userMapper.insertUser(user);
        }
        throw new UserIdDuplicateException();
    }

    /**
     * 아이디 중복확인
     *
     * @param userId
     * @return 중복된 아이디가 있을시에 true 없을시에 false
     */
    @Override
    public boolean isDuplicateUserId(String userId) {
        User findUser = userMapper.findById(userId);
        return findUser != null;
    }


}
