package kr.flab.tradingmarket.domain.product.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import kr.flab.tradingmarket.common.annotation.ValidCategory;
import kr.flab.tradingmarket.common.annotation.ValidEnum;
import kr.flab.tradingmarket.domain.product.entity.ProductExchangeStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class RegisterProductDto {

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

}

