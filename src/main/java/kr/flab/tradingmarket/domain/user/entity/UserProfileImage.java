package kr.flab.tradingmarket.domain.user.entity;

import java.time.LocalDateTime;

import kr.flab.tradingmarket.domain.image.entity.BaseImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileImage extends BaseImage {

    @Builder
    public UserProfileImage(Long imageNo, String originalFileName, String fileLink, Long fileSize,
        LocalDateTime createDate, LocalDateTime modifyDate) {
        super(imageNo, originalFileName, fileLink, fileSize,
            createDate, modifyDate);
    }
}
