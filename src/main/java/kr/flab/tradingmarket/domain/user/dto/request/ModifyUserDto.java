package kr.flab.tradingmarket.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

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
public class ModifyUserDto {
    @Length(min = 2, max = 19)
    @NotBlank
    private String userName;

    @PhonePatternNotBlank
    private String userPhone;
}
