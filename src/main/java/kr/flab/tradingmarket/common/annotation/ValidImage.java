package kr.flab.tradingmarket.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.flab.tradingmarket.common.validator.ImageFileValidator;

@Constraint(validatedBy = ImageFileValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {
    String message() default "이미지는 1개 이상 10개 이하만 업로드 할 수있습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}