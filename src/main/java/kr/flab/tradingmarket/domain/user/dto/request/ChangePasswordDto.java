package kr.flab.tradingmarket.domain.user.dto.request;

import javax.validation.constraints.AssertTrue;

import kr.flab.tradingmarket.common.annotation.PasswordPatternNotBlack;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@EqualsAndHashCode
public class ChangePasswordDto {
    @PasswordPatternNotBlack
    private String currentPassword;

    @PasswordPatternNotBlack
    private String password;

    @PasswordPatternNotBlack
    private String confirmPassword;

    @AssertTrue(message = "재입력한 비밀번호가 일치하지 않습니다.")
    private boolean isPasswordEqual() {
        return password.equals(confirmPassword);
    }
}
