package kr.flab.tradingmarket.domain.user.dto.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode
@ToString
@Getter
@Builder
public class UserAuthDto {
    @Length(min = 8, max = 20)
    @NotBlank
    private String userId;

    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}",
            message = "비밀번호는 영문과 숫자 조합으로 8 ~ 16로 입력해주세요.")
    private String userPassword;
}
