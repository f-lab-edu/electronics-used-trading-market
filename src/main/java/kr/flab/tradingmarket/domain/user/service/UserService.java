package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.entity.User;


public interface UserService {
    Long joinUser(User user);


    boolean isDuplicateUserId(String userId);

}
