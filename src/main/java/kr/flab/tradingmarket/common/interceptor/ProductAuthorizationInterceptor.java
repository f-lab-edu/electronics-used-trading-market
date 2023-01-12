package kr.flab.tradingmarket.common.interceptor;

import static org.springframework.web.servlet.HandlerMapping.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.flab.tradingmarket.common.annotation.ProductAuthCheck;
import kr.flab.tradingmarket.common.exception.NoPermissionException;
import kr.flab.tradingmarket.domain.product.service.ProductService;
import kr.flab.tradingmarket.domain.user.service.LoginService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductAuthorizationInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final ProductService productService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod && ((HandlerMethod)handler).hasMethodAnnotation(ProductAuthCheck.class)) {
            Map<String, String> pathVariables = (Map<String, String>)request.getAttribute(
                URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long productNo = Long.valueOf(pathVariables.get("productNo"));
            if (!productService.isProductAuthorized(productNo, loginService.getLoginUserNo())) {
                throw new NoPermissionException("You are not entitled to that product.");
            }
        }
        return true;
    }

}


