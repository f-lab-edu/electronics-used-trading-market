package kr.flab.tradingmarket.domain.product.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kr.flab.tradingmarket.domain.category.entity.Category;
import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = {"productNo"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    private Long productNo;
    private String productName;
    private LocalDate productAsExpirationDate;
    private ProductStatus productStatus;
    private ProductExchangeStatus productExchangeStatus;
    private LocalDate purchaseDate;
    private ProductSalesStatus productSalesStatus;
    private BigDecimal productPrice;
    private String productContent;
    private Integer productStock;
    private Integer productViewCount;
    private LocalDateTime modifyDate;
    private LocalDateTime createDate;
    private ProductImage productThumbnail;
    private Category category;
    private User seller;

    @Builder
    public Product(Long productNo, String productName, LocalDate productAsExpirationDate, ProductStatus productStatus,
        ProductExchangeStatus productExchangeStatus, LocalDate purchaseDate, ProductSalesStatus productSalesStatus,
        BigDecimal productPrice, String productContent, Integer productStock, Integer productViewCount,
        LocalDateTime modifyDate, LocalDateTime createDate, ProductImage productThumbnail, Category category,
        User seller) {
        this.productNo = productNo;
        this.productName = productName;
        this.productAsExpirationDate = productAsExpirationDate;
        this.productStatus = productStatus;
        this.productExchangeStatus = productExchangeStatus;
        this.purchaseDate = purchaseDate;
        this.productSalesStatus = productSalesStatus;
        this.productPrice = productPrice;
        this.productContent = productContent;
        this.productStock = productStock;
        this.productViewCount = productViewCount;
        this.modifyDate = modifyDate;
        this.createDate = createDate;
        this.productThumbnail = productThumbnail;
        this.category = category;
        this.seller = seller;
    }

    public static Product of(RegisterProductDto registerProductDto, Long sellerNo) {
        return Product.builder()
            .productName(registerProductDto.getProductName())
            .productAsExpirationDate(registerProductDto.getProductAsExpirationDate())
            .productStatus(ProductStatus.valueOf(registerProductDto.getProductStatus()))
            .productExchangeStatus(ProductExchangeStatus.valueOf(registerProductDto.getProductExchangeStatus()))
            .purchaseDate(registerProductDto.getPurchaseDate())
            .productSalesStatus(ProductSalesStatus.SALE)
            .productPrice(registerProductDto.getProductPrice())
            .productContent(registerProductDto.getProductContent())
            .productViewCount(0)
            .productStock(registerProductDto.getProductStock())
            .category(Category.builder().categoryNo(registerProductDto.getProductCategoryNo()).build())
            .seller(User.builder().userNo(sellerNo).build())
            .build();
    }
}