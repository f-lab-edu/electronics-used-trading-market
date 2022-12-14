package kr.flab.tradingmarket.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import kr.flab.tradingmarket.common.annotation.PasswordPatternNotBlack;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@Builder
public class UserAuthDto {
    @Length(min = 8, max = 20)
    @NotBlank
    private String userId;

    @PasswordPatternNotBlack
    private String userPassword;
}
