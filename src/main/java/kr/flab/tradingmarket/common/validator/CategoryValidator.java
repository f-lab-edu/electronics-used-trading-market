package kr.flab.tradingmarket.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import kr.flab.tradingmarket.common.annotation.ValidCategory;
import kr.flab.tradingmarket.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CategoryValidator implements ConstraintValidator<ValidCategory, Long> {

    private final CategoryService categoryService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return categoryService.existCategory(value);
    }

}