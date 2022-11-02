package kr.flab.tradingmarket.domain.user.service;

import kr.flab.tradingmarket.domain.user.entity.User;

/**
 * packageName :  kr.flab.tradingmarket.domain.user.service
 * fileName : UserService
 * author :  ddh96
 * date : 2022-11-02
 * description : UserService interface
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-11-02                ddh96             최초 생성
 */
public interface UserService {
    Long joinUser(User user);


    boolean isDuplicateUserId(String userId);

}
