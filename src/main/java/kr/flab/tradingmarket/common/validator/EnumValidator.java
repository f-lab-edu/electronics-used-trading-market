package kr.flab.tradingmarket.common.validator;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.flab.tradingmarket.common.annotation.ValidEnum;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> values;
    private boolean nullable;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.values = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
            .map(Enum::name)
            .collect(Collectors.toSet());
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (nullable && value == null) || values.contains(value);
    }

}