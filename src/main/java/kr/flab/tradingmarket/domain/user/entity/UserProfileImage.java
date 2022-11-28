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


    @Builder
    public UserProfileImage(Long imageNo, String originalFileName, String fileLink, Long fileSize, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.imageNo = imageNo;
        this.originalFileName = originalFileName;
        this.fileLink = fileLink;
        this.fileSize = fileSize;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }
}
