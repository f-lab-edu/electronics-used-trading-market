package kr.flab.tradingmarket.domain.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.entity.ProductExchangeStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseModifyProductDto {

    private Long productNo;
    private String productName;
    private LocalDate productAsExpirationDate;
    private ProductStatus productStatus;
    private ProductExchangeStatus productExchangeStatus;
    private LocalDate purchaseDate;
    private BigDecimal productPrice;
    private String productContent;
    private Integer productStock;
    private Long categoryNo;
    private Long sellerNo;
    private List<ProductImageSimpleDto> imageList;
    private ProductImageSimpleDto thumbnail;

    public static ResponseModifyProductDto from(Product product) {

        List<ProductImageSimpleDto> imageList = new ArrayList<>();
        ProductImageSimpleDto thumbnail = imageSeparation(product, imageList);

        return ResponseModifyProductDto.builder()
            .productNo(product.getProductNo())
            .productName(product.getProductName())
            .productAsExpirationDate(product.getProductAsExpirationDate())
            .productStatus(product.getProductStatus())
            .productExchangeStatus(product.getProductExchangeStatus())
            .purchaseDate(product.getPurchaseDate())
            .productPrice(product.getProductPrice())
            .productContent(product.getProductContent())
            .productStock(product.getProductStock())
            .categoryNo(product.getCategory().getCategoryNo())
            .sellerNo(product.getSeller().getUserNo())
            .imageList(imageList)
            .thumbnail(thumbnail)
            .build();
    }

    private static ProductImageSimpleDto imageSeparation(Product product, List<ProductImageSimpleDto> imageList) {
        ProductImageSimpleDto thumbnail = null;
        for (ProductImage productImage : product.getImageList()) {
            if (productImage.getImageNo().equals(product.getProductThumbnail().getImageNo())) {
                thumbnail = ProductImageSimpleDto.from(productImage);
                continue;
            }
            imageList.add(ProductImageSimpleDto.from(productImage));
        }
        return thumbnail;
    }
}
