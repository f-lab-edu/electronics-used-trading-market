package kr.flab.tradingmarket.domain.product.entity;

import java.time.LocalDateTime;

import kr.flab.tradingmarket.domain.image.entity.BaseImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends BaseImage {

    private Long productNo;

    @Builder
    public ProductImage(Long imageNo, Long productNo, String originalFileName, String fileLink, Long fileSize,
        LocalDateTime createDate, LocalDateTime modifyDate) {
        super(imageNo, originalFileName, fileLink, fileSize,
            createDate, modifyDate);
        this.productNo = productNo;
    }

    public void setProductNo(Long productNo) {
        this.productNo = productNo;
    }
}
