package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class RedisLoginService implements LoginService {
    public static final String USER_ID = "USER_ID";
    private final UserService userService;
    private final HttpSession httpSession;

    @Override
    public Long getLoginUserId() {
        return (Long) httpSession.getAttribute(USER_ID);
    }

    @Override
    public void login(UserAuthDto loginReq) {
        Long userNo = userService.userAuthCheck(loginReq);
        httpSession.setAttribute(USER_ID, userNo);

    }

    @Override
    public void logout() {
        httpSession.removeAttribute(USER_ID);
    }
}
