package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class RedisLoginService implements LoginService {
    public static final String USER_ID = "USER_ID";
    private final HttpSession httpSession;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public Long getLoginUserId() {
        return (Long) httpSession.getAttribute(USER_ID);
    }

    @Override
    public void login(UserAuthDto loginReq) {
        Long userNo = loginCheck(loginReq);
        httpSession.setAttribute(USER_ID, userNo);

    }

    @Override
    public void logout() {
        httpSession.removeAttribute(USER_ID);
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
