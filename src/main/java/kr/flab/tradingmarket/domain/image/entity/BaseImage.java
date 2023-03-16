package kr.flab.tradingmarket.domain.image.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = {"imageNo"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseImage {
    private Long imageNo;
    private String originalFileName;
    private String fileLink;
    private Long fileSize;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public static BaseImage of(String originalFileName, String fileLink, Long fileSize) {
        return new BaseImage(null, originalFileName, fileLink, fileSize, null, null);
    }
}
