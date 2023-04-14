package kr.flab.tradingmarket.domain.user.service;

import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisLoginService implements LoginService {
    public static final String USER_NO = "USER_NO";
    private final HttpSession httpSession;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public Long getLoginUserNo() {
        return (Long)httpSession.getAttribute(USER_NO);
    }

    @Override
    @Transactional(readOnly = true)
    public void login(UserAuthDto loginReq) {
        Long userNo = loginCheck(loginReq);
        httpSession.setAttribute(USER_NO, userNo);
    }

    @Override
    public void logout() {
        httpSession.removeAttribute(USER_NO);
    }

    private Long loginCheck(UserAuthDto userAuth) {
        User findUser = findByUserId(userAuth.getUserId());
        if (!passwordEncoder.matches(userAuth.getUserPassword(), findUser.getUserPassword())) {
            throw new PasswordNotMatchException("Password Not Matches");
        }
        return findUser.getUserNo();
    }

    private User findByUserId(String userId) {
        User findUser = userMapper.findById(userId);
        if (findUser == null) {
            throw new UserNotFoundException("User Not Found");
        }
        return findUser;
    }

}
