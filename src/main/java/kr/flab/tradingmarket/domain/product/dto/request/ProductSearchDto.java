package kr.flab.tradingmarket.domain.product.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import kr.flab.tradingmarket.common.annotation.ValidEnum;
import kr.flab.tradingmarket.domain.product.entity.ProductExchangeStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductSalesStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchDto {
    @Length(min = 2)
    @NotNull
    private String keyword;

    private Integer category;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate productAsExpirationDate;

    @ValidEnum(enumClass = ProductStatus.class, nullable = true)
    private String productStatus;

    @ValidEnum(enumClass = ProductExchangeStatus.class, nullable = true)
    private String productExchangeStatus;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate purchaseDate;

    private BigDecimal maxPrice;

    private BigDecimal minPrice;

    @NotNull
    @ValidEnum(enumClass = SearchOrder.class, nullable = true)
    private String order;

    @ValidEnum(enumClass = ProductSalesStatus.class, nullable = true)
    private String productSalesStatus;

    private String lastModifiedDate;

    private BigDecimal lastPrice;

    private Integer lastLikes;

    private Long lastProductNo;

    @Max(30)
    @Min(1)
    @NotNull
    private Integer size;

    public SearchOrder getOrder() {
        return SearchOrder.valueOf(this.order);
    }

    public boolean isPopularOrder() {
        return this.getOrder() == SearchOrder.POPULAR && this.getLastLikes() != null
            && this.getLastProductNo() != null;
    }

    public boolean isPriceOrder() {
        return (this.getOrder() == SearchOrder.PRICE_DESC || this.getOrder() == SearchOrder.PRICE_ASC)
            && this.getLastPrice() != null && this.getLastProductNo() != null;
    }

    public boolean isLatestDateOrder() {
        return this.getOrder() == SearchOrder.DATE && this.getLastModifiedDate() != null
            && this.getLastProductNo() != null;
    }
}
