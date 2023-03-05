package kr.flab.tradingmarket.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import kr.flab.tradingmarket.common.annotation.PhonePatternNotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyUserDto {
    @Length(min = 2, max = 19)
    @NotBlank
    private String userName;

    @PhonePatternNotBlank
    private String userPhone;
}
