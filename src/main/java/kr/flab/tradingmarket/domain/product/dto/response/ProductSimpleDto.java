package kr.flab.tradingmarket.domain.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.flab.tradingmarket.domain.product.entity.ProductSalesStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductSearch;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "productNo")
public class ProductSimpleDto {
    private Long productNo;
    private String productName;
    private BigDecimal productPrice;
    private LocalDateTime modifyDate;
    private String image;
    private Integer likeCount;
    private Boolean myWish;
    private ProductSalesStatus productSalesStatus;

    @Builder
    public ProductSimpleDto(Long productNo, String productName, BigDecimal productPrice, LocalDateTime modifyDate,
        String image, Integer likeCount, Boolean myWish, ProductSalesStatus productSalesStatus) {
        this.productNo = productNo;
        this.productName = productName;
        this.productPrice = productPrice;
        this.modifyDate = modifyDate;
        this.image = image;
        this.likeCount = likeCount;
        this.myWish = myWish;
        this.productSalesStatus = productSalesStatus;
    }

    public static ProductSimpleDto of(ProductSearch productSearch, boolean myWish) {
        return ProductSimpleDto.builder()
            .productNo(productSearch.getProductNo())
            .productName(productSearch.getProductName())
            .productPrice(productSearch.getProductPrice())
            .modifyDate(productSearch.getModifyDate())
            .image(productSearch.getImage())
            .likeCount(productSearch.getLikeCount())
            .myWish(myWish)
            .productSalesStatus(productSearch.getProductSalesStatus())
            .build();
    }
}