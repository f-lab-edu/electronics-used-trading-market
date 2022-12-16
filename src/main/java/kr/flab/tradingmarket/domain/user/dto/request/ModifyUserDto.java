package kr.flab.tradingmarket.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import kr.flab.tradingmarket.common.annotation.PhonePatternNotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@EqualsAndHashCode
public class ModifyUserDto {
    @Length(min = 2, max = 19)
    @NotBlank
    private String userName;

    @PhonePatternNotBlank
    private String userPhone;
}
