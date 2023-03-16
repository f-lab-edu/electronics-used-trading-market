package kr.flab.tradingmarket.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.flab.tradingmarket.common.code.ValidationType;
import kr.flab.tradingmarket.common.validator.CategoryValidator;

@Constraint(validatedBy = CategoryValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategory {
    String message() default "존재하지 않는 카테고리 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ValidationType type() default ValidationType.INSERT;
}