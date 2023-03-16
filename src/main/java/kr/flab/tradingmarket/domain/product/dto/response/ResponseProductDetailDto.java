package kr.flab.tradingmarket.domain.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import kr.flab.tradingmarket.domain.category.entity.Category;
import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.entity.ProductExchangeStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductSalesStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseProductDetailDto {
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
    private Long categoryNo;
    private String categoryName;
    private Long sellerNo;
    private String sellerName;
    private Integer likeCount;
    private List<ResponseImageSimple> imageList;

    public static ResponseProductDetailDto from(Product product) {

        Category category = product.getCategory();
        return ResponseProductDetailDto.builder()
            .productNo(product.getProductNo())
            .productName(product.getProductName())
            .productAsExpirationDate(product.getProductAsExpirationDate())
            .productStatus(product.getProductStatus())
            .productExchangeStatus(product.getProductExchangeStatus())
            .purchaseDate(product.getPurchaseDate())
            .productPrice(product.getProductPrice())
            .productContent(product.getProductContent())
            .productStock(product.getProductStock())
            .productSalesStatus(product.getProductSalesStatus())
            .productViewCount(product.getProductViewCount())
            .modifyDate(product.getModifyDate())
            .categoryNo(category.getCategoryNo())
            .likeCount(product.getLikeCount())
            .categoryName(categoryFormatter(category))
            .sellerNo(product.getSeller().getUserNo())
            .sellerName(product.getSeller().getUserName())
            .imageList(product.getImageList()
                .stream()
                .map(i -> new ResponseImageSimple(i.getImageNo(), i.getFileLink()))
                .toList())
            .build();
    }

    private static String categoryFormatter(Category category) {
        StringBuilder sb = new StringBuilder();
        appendCategory(sb, category.getFirstCategory());
        appendCategory(sb, category.getSecondCategory());
        appendCategory(sb, category.getThirdCategory());
        return sb.toString();
    }

    private static void appendCategory(StringBuilder sb, String category) {
        if (category == null || category.isEmpty()) {
            return;
        }
        if (sb.length() > 0) {
            sb.append(" > ");
        }
        sb.append(category);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class ResponseImageSimple {
        private Long imageNo;
        private String fileLink;
    }
}
