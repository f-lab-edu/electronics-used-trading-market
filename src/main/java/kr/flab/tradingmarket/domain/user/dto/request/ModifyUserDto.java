package kr.flab.tradingmarket.domain.user.dto.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@Builder
@EqualsAndHashCode
public class ModifyUserDto {
    @Length(min = 2, max = 19)
    @NotBlank
    private String userName;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11,11}",
            message = "전화번호는 숫자 11자리로 입력해주세요.")
    private String userPhone;


}
