package kr.flab.tradingmarket.domain.product.dto;

import static kr.flab.tradingmarket.domain.product.controller.ProductControllerFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterProductDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("productStatus가 잘못된 값 일때 : validation 실패 테스트")
    void failProductStatusByInvalidStateValue() {
        Set<ConstraintViolation<RegisterProductDto>> violations = validator.validate(
            FAIL_VALIDATION_PRODUCT_STATUS);
        assertThat("잘못된 상태값 입니다.").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("productExchangeStatus가 잘못된 값 일때 : validation 실패 테스트")
    void failProductExchangeStatusByInvalidStateValue() {
        Set<ConstraintViolation<RegisterProductDto>> violations = validator.validate(
            FAIL_VALIDATION_PRODUCT_EXCHANGE_STATUS);
        assertThat("잘못된 상태값 입니다.").isEqualTo(violations.iterator().next().getMessage());
    }

}