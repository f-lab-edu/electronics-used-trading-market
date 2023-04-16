package kr.flab.tradingmarket.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import kr.flab.tradingmarket.domain.user.exception.UserAccessDeniedException;
import kr.flab.tradingmarket.domain.user.service.LoginService;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LoginCheckAop {

    private final LoginService loginService;

    @Around("@annotation(kr.flab.tradingmarket.common.annotation.AuthCheck)")
    public Object loginChecker(ProceedingJoinPoint joinPoint) throws Throwable {

        if (loginService.getLoginUserNo() == null) {
            throw new UserAccessDeniedException("No session");
        }

        return joinPoint.proceed();
    }
}
