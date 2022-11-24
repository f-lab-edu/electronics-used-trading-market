package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
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
     * @param userDto
     * @return 생성된 user_no 리턴
     * @throws UserIdDuplicateException 아이디 중복시에 리턴
     */
    @Override
    public Long joinUser(JoinUserDto userDto) {

        if (isDuplicateUserId(userDto.getUserId())) {
            throw new UserIdDuplicateException();
        }
        User user = User.from(userDto);
        user.setEncryptionPassword(passwordEncoder.encode(user.getUserPassword()));
        Long saveUserId = null;
        try {
            saveUserId = userMapper.insertUser(user);
        } catch (DuplicateKeyException e) {
            throw new UserIdDuplicateException("Duplicated userId", e);
        }
        return saveUserId;

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

    /**
     * 유저 인증 정보 확인
     *
     * @param userAuth id,password dto
     * @return user 인증이 성공하면 userNo 리턴
     */
    @Override
    public Long userAuthCheck(UserAuthDto userAuth) {
        User findUser = findByUserId(userAuth.getUserId());
        if (!passwordEncoder.matches(userAuth.getUserPassword(), findUser.getUserPassword())) {
            throw new PasswordNotMatchException("Password Not Matches");
        }
        return findUser.getUserNo();

    }

    @Override
    public User findByUserId(String userId) {
        User findUser = userMapper.findById(userId);
        if (findUser == null) {
            throw new UserNotFoundException("User Not Found");
        }
        return findUser;
    }

    @Override
    public User findByUserNo(Long userNo) {
        User findUser = userMapper.findByNo(userNo);
        if (findUser == null) {
            throw new UserNotFoundException("User Not Found");
        }
        return findUser;
    }

}
