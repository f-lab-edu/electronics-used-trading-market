package kr.flab.tradingmarket.domain.user.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * packageName :  kr.flab.tradingmarket.domain.user.entity
 * fileName : UserProfileImage
 * author :  ddh96
 * date : 2022-11-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-11-02                ddh96             최초 생성
 */
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
