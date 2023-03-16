package kr.flab.tradingmarket.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import kr.flab.tradingmarket.common.annotation.PasswordPatternNotBlack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAuthDto {
    @Length(min = 8, max = 20)
    @NotBlank
    private String userId;

    @PasswordPatternNotBlack
    private String userPassword;
}
