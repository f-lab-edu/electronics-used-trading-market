package kr.flab.tradingmarket.domain.product.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import kr.flab.tradingmarket.common.annotation.ValidCategory;
import kr.flab.tradingmarket.common.annotation.ValidEnum;
import kr.flab.tradingmarket.domain.product.entity.ProductExchangeStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductSalesStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestModifyProductDto {

    @Length(min = 1, max = 99)
    @NotBlank
    private String productName;
    @NotNull
    @ValidCategory
    private Long productCategoryNo;
    @NotNull
    private LocalDate productAsExpirationDate;
    @ValidEnum(enumClass = ProductStatus.class)
    private String productStatus;
    @NotNull
    private Integer productStock;
    @ValidEnum(enumClass = ProductExchangeStatus.class)
    private String productExchangeStatus;
    @NotNull
    private LocalDate purchaseDate;
    @NotNull
    private BigDecimal productPrice;
    @NotBlank
    private String productContent;
    @ValidEnum(enumClass = ProductSalesStatus.class)
    private String productSalesStatus;
    @Size(max = 10)
    @NotNull
    private List<Long> removeImageNoList;
    @Valid
    @NotNull
    private UpdateImage updateThumbnail;

    public ProductStatus getProductStatus() {
        return ProductStatus.valueOf(productStatus);
    }

    public ProductExchangeStatus getProductExchangeStatus() {
        return ProductExchangeStatus.valueOf(productExchangeStatus);
    }

    public ProductSalesStatus getProductSalesStatus() {
        return ProductSalesStatus.valueOf(productSalesStatus);
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UpdateImage {

        private String imageName;
        private Long imageNo;

        @NotBlank
        @ValidEnum(enumClass = UpdateType.class)
        private String updateType;

        public UpdateType getUpdateType() {
            return UpdateType.valueOf(this.updateType);
        }

        public enum UpdateType {
            NEW, OLD, NONE
        }

    }

}
