package kr.flab.tradingmarket.domain.user.dto;

import static kr.flab.tradingmarket.domain.user.config.UserCommonFixture.*;
import static kr.flab.tradingmarket.domain.user.controller.UserControllerTestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;

class UserValidationTest {
    Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        Locale.setDefault(Locale.KOREA);
    }

    @Test
    @DisplayName("userPassword패턴이 일치하지 않을 때 : validation 실패 테스트")
    void failPasswordByPatternDoesNotMatch() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_WRONG_PATTERN_PASSWORD);
        assertThat("비밀번호는 영문과 숫자 조합으로 8 ~ 16자리까지 가능합니다.").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("userPassword가 공백일때 : validation 실패 테스트")
    void failPasswordByBlank() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_BLANK_PASSWORD);
        assertThat("공백일 수 없습니다").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("userPhone패턴이 일치하지 않을 때 : validation 실패 테스트")
    void failPhoneByPatternDoesNotMatch() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_WRONG_PATTERN_PHONE);
        assertThat("전화번호는 숫자 11자리로 입력해주세요.").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("userPhone이 공백일때 : validation 실패 테스트")
    void failPhoneByBlank() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_BLANK_PHONE);
        assertThat("공백일 수 없습니다").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("JoinUserDto : validation 성공 테스트")
    void successfulJoinUserDto() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(DEFAULT_JOIN_USER_DTO);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("비밀번호와 재입력한 비밀번호가 다를 때 : validation 실패 테스트")
    void validationPasswordEqual() {
        Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(
            FAIL_VALIDATION_NON_EQUALS_PASSWORD);
        assertThat("재입력한 비밀번호가 일치하지 않습니다.").isEqualTo(violations.iterator().next().getMessage());
    }
}
