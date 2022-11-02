package kr.flab.tradingmarket.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName :  kr.flab.tradingmarket.domain.user.exception
 * fileName : UserIdDuplicateException
 * author :  ddh96
 * date : 2022-11-02
 * description : 유저 아이디 중복 예외
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-11-02                ddh96             최초 생성
 */
@AllArgsConstructor
@Getter
public class UserIdDuplicateException extends RuntimeException {
}
