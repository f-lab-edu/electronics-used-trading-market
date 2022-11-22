package kr.flab.tradingmarket.common.annotation;


import java.lang.annotation.*;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthCheck {
}
