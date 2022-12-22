package kr.flab.tradingmarket.domain.product.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = {"imageNo"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ProductImage {
    private Long imageNo;
    private Long productNo;
    private String originalFileName;
    private String fileLink;
    private Long fileSize;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @Builder
    public ProductImage(Long imageNo, Long productNo, String originalFileName, String fileLink, Long fileSize,
        LocalDateTime createDate, LocalDateTime modifyDate) {
        this.imageNo = imageNo;
        this.productNo = productNo;
        this.originalFileName = originalFileName;
        this.fileLink = fileLink;
        this.fileSize = fileSize;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public void setProductNo(Long productNo) {
        this.productNo = productNo;
    }
}
