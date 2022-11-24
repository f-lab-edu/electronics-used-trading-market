package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;

public interface LoginService {
    Long getLoginUserId();

    void login(UserAuthDto loginReq);

    void logout();

}
