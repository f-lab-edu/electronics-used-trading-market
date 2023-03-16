package kr.flab.tradingmarket.domain.user.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import kr.flab.tradingmarket.common.annotation.PasswordPatternNotBlack;
import kr.flab.tradingmarket.common.annotation.PhonePatternNotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinUserDto {
    @Length(min = 8, max = 20)
    @NotBlank
    private String userId;

    @Length(min = 2, max = 19)
    @NotBlank
    private String userName;

    @NotBlank
    @PasswordPatternNotBlack
    private String userPassword;

    @PhonePatternNotBlank
    private String userPhone;

    @NotNull
    private LocalDate userBirth;
}



