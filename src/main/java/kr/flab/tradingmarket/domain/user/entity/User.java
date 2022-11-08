package kr.flab.tradingmarket.domain.user.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class User {

    private Long userNo;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,20}",
            message = "아이디는 영문과 숫자 조합으로 8 ~ 20자리까지 가능합니다.")
    @NotBlank
    private String userId;

    @Length(max = 19)
    @NotBlank
    private String userName;
    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}",
            message = "비밀번호는 영문과 숫자 조합으로 8 ~ 16자리까지 가능합니다.")
    private String userPassword;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11,11}",
            message = "전화번호는 숫자 11자리로 입력해주세요")
    private String userPhone;

    @NotNull
    private LocalDate userBirth;


    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private UserProfileImage userProfileImage;

    public void setEncryptionPassword(String encryptionPassword) {
        this.userPassword = encryptionPassword;
    }

}
