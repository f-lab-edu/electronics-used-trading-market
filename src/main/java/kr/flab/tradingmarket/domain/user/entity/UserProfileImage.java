package kr.flab.tradingmarket.domain.user.entity;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UserProfileImage {
    private Long imageNo;
    private String originalFileName;
    private String fileLink;
    private Long fileSize;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
