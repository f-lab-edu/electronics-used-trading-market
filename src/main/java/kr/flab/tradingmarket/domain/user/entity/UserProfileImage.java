package kr.flab.tradingmarket.domain.user.entity;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@EqualsAndHashCode(of = {"imageNo"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString //테스트용
public class UserProfileImage {
    private Long imageNo;
    private String originalFileName;
    private String fileLink;
    private Long fileSize;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
