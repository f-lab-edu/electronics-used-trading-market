package kr.flab.tradingmarket.common.aop;

import kr.flab.tradingmarket.domain.user.exception.UserAccessDeniedException;
import kr.flab.tradingmarket.domain.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoginCheckAop {

    private final LoginService loginService;


    @Around("@annotation(kr.flab.tradingmarket.common.annotation.AuthCheck)")
    public Object LoginChecker(ProceedingJoinPoint joinPoint) throws Throwable {

        if (loginService.getLoginUserId() == null) {
            throw new UserAccessDeniedException("No session");
        }

        return joinPoint.proceed();
    }
}
