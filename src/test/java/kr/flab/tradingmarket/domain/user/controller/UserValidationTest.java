package kr.flab.tradingmarket.domain.user.controller;

import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static kr.flab.tradingmarket.domain.user.controller.UserControllerTestFixture.*;
import static kr.flab.tradingmarket.domain.user.service.UserServiceTestFixture.SUCCESSFUL_JOIN_USER_DTO;
import static org.assertj.core.api.Assertions.assertThat;

public class UserValidationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Test
    @DisplayName("joinDto : validation 성공 테스트")
    void successValidation() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(SUCCESSFUL_JOIN_USER_DTO);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("userId field 길이가 8자 이하 : validation 실패 테스트")
    void validationUserId() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_SHORT_ID);
        assertThat("길이가 8에서 20 사이여야 합니다").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("userBirth field 빈값 : validation 실패 테스트")
    void validationUserBirth() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_BIRTH_NULL);
        assertThat("널이어서는 안됩니다").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("userName field 길이가 19자 이상 : validation 실패 테스트")
    void validationUserName() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_LONG_USERNAME);
        assertThat("길이가 2에서 19 사이여야 합니다").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("userPassword field 패턴이 일치하지 않을 때 : validation 실패 테스트")
    void validationPassword() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_WRONG_PATTERN_PASSWORD);
        assertThat("비밀번호는 영문과 숫자 조합으로 8 ~ 16자리까지 가능합니다.").isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("userPhone field 패턴이 일치하지 않을 때 : validation 실패 테스트")
    void validationUserPhone() {
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(FAIL_VALIDATION_WRONG_PATTERN_PHONE);
        assertThat("전화번호는 숫자 11자리로 입력해주세요.").isEqualTo(violations.iterator().next().getMessage());
    }


    @Test
    @DisplayName("비밀번호와 재입력한 비밀번호가 다를 때 : validation 실패 테스트")
    void validationPasswordEqual() {
        Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(FAIL_VALIDATION_NON_EQUALS_PASSWORD);
        assertThat("재입력한 비밀번호가 일치하지 않습니다.").isEqualTo(violations.iterator().next().getMessage());
    }


}
