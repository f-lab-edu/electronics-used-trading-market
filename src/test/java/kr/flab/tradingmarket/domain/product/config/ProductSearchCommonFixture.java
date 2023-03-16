package kr.flab.tradingmarket.domain.product.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kr.flab.tradingmarket.domain.like.entity.Like;
import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.request.SearchOrder;
import kr.flab.tradingmarket.domain.product.dto.response.ProductSimpleDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponsePageData;
import kr.flab.tradingmarket.domain.product.entity.ProductSalesStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductSearch;

public class ProductSearchCommonFixture {

    public static final ProductSearchDto DEFAULT_PAGE_POPULAR_PRODUCT_SEARCH_DTO = ProductSearchDto.builder()
        .keyword("test_search")
        .category(1)
        .productAsExpirationDate(LocalDate.parse("2020-11-10"))
        .productStatus("HIGH")
        .productSalesStatus("SALE")
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.parse("2020-10-30"))
        .maxPrice(BigDecimal.valueOf(50000))
        .minPrice(BigDecimal.valueOf(10000))
        .order("POPULAR")
        .size(10)
        .build();
    public static final ProductSearchDto DEFAULT_PAGE_DATE_PRODUCT_SEARCH_DTO = ProductSearchDto.builder()
        .keyword("test_search")
        .category(1)
        .productAsExpirationDate(LocalDate.parse("2020-11-10"))
        .productStatus("HIGH")
        .productSalesStatus("SALE")
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.parse("2020-10-30"))
        .maxPrice(BigDecimal.valueOf(50000))
        .minPrice(BigDecimal.valueOf(10000))
        .order("DATE")
        .size(10)
        .build();
    public static final ProductSearchDto DEFAULT_PAGE_PRICE_ASC_PRODUCT_SEARCH_DTO = ProductSearchDto.builder()
        .keyword("test_search")
        .category(1)
        .productAsExpirationDate(LocalDate.parse("2020-11-10"))
        .productStatus("HIGH")
        .productSalesStatus("SALE")
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.parse("2020-10-30"))
        .maxPrice(BigDecimal.valueOf(50000))
        .minPrice(BigDecimal.valueOf(10000))
        .order("PRICE_ASC")
        .size(10)
        .build();
    public static final ProductSearchDto DEFAULT_PAGE_PRICE_DESC_PRODUCT_SEARCH_DTO = ProductSearchDto.builder()
        .keyword("test_search")
        .category(1)
        .productAsExpirationDate(LocalDate.parse("2020-11-10"))
        .productStatus("HIGH")
        .productSalesStatus("SALE")
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.parse("2020-10-30"))
        .maxPrice(BigDecimal.valueOf(50000))
        .minPrice(BigDecimal.valueOf(10000))
        .order("PRICE_DESC")
        .size(10)
        .build();

    public static final List<ProductSimpleDto> DEFAULT_FIND_SEARCH_PRODUCT_LIST = new ArrayList<>();
    public static final ResponsePageData<Integer> LAST_POPULAR_PAGE_DATA = new ResponsePageData<>(10,
        SearchOrder.POPULAR,
        10L);
    public static final ResponsePageData<LocalDateTime> LAST_DATE_PAGE_DATA = new ResponsePageData<>(
        LocalDateTime.of(2023, 10, 10, 23, 20, 20),
        SearchOrder.DATE,
        10L);
    public static final ResponsePageData<BigDecimal> LAST_PRICE_DESC_PAGE_DATA = new ResponsePageData<>(
        BigDecimal.valueOf(20010),
        SearchOrder.PRICE_DESC,
        10L);
    public static final ResponsePageData<BigDecimal> LAST_PRICE_ASC_PAGE_DATA = new ResponsePageData<>(
        BigDecimal.valueOf(20010),
        SearchOrder.PRICE_ASC,
        10L);
    public static final List<ProductSearch> DEFAULT_PRODUCT_SEARCH_ENTITY = new ArrayList<>();
    public static final List<Like> DEFAULT_LIKE_LIST = new ArrayList<>();

    static {
        for (int i = 1; i <= 10; i++) {
            LocalDateTime testDateTime = LocalDateTime.of(2023, 10, i, 23, 20, 20);
            ProductSearch build = ProductSearch.builder()
                .productNo((long)i)
                .productName("test_item_" + i)
                .productPrice(BigDecimal.valueOf(20000 + i))
                .modifyDate(testDateTime)
                .image("image_link_" + i)
                .modifyDate(testDateTime)
                .likeCount(i)
                .productSalesStatus(ProductSalesStatus.SALE)
                .build();

            DEFAULT_FIND_SEARCH_PRODUCT_LIST.add(ProductSimpleDto.of(build, true));
            DEFAULT_PRODUCT_SEARCH_ENTITY.add(build);
            DEFAULT_LIKE_LIST.add(Like.builder()
                .likeNo((long)i)
                .userNo(1L)
                .productNo((long)i)
                .createDate(testDateTime)
                .build());
        }
    }

}
