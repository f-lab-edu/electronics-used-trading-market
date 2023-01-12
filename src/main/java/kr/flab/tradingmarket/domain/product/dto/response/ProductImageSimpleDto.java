package kr.flab.tradingmarket.domain.product.dto.response;

import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductImageSimpleDto {
    private Long imageNo;
    private String fileName;
    private String fileLink;

    public static ProductImageSimpleDto from(ProductImage productImage) {
        return ProductImageSimpleDto.builder()
            .imageNo(productImage.getImageNo())
            .fileName(productImage.getOriginalFileName())
            .fileLink(productImage.getFileLink())
            .build();
    }
}
