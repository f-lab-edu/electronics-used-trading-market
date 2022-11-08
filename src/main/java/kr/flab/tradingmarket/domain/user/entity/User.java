package kr.flab.tradingmarket.domain.user.entity;

import kr.flab.tradingmarket.domain.user.controller.request.JoinUserDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@EqualsAndHashCode(of = {"userNo"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString //테스트용
public class User {

    private Long userNo;
    private String userId;
    private String userName;
    private String userPassword;
    private String userPhone;
    private LocalDate userBirth;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private UserProfileImage userProfileImage;

    @Builder
    public User(Long userNo, String userId, String userName, String userPassword, String userPhone, LocalDate userBirth, LocalDateTime createDate, LocalDateTime modifyDate, UserProfileImage userProfileImage) {
        this.userNo = userNo;
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userBirth = userBirth;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.userProfileImage = userProfileImage;
    }

    static public User from(JoinUserDto dto) {
        return User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .userPassword(dto.getUserPassword())
                .userPhone(dto.getUserPhone())
                .userBirth(dto.getUserBirth())
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();
    }

    public void setEncryptionPassword(String encryptionPassword) {
        this.userPassword = encryptionPassword;
    }

}
