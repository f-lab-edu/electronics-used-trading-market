package kr.flab.tradingmarket.domain.product.controller;

import static kr.flab.tradingmarket.domain.product.config.ProductSearchCommonFixture.*;

import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductSimpleDto;

public class ProductSearchControllerFixture {
    public static final String PRODUCT_SEARCH_URL = "/products/search";
    public static final String VERSION1 = "application/vnd.mymarket.appv1+json";
    public static final String VERSION2 = "application/vnd.mymarket.appv2+json";
    public static final String VERSION3 = "application/vnd.mymarket.appv3+json";

    public static final ResponseProductSimpleDto DEFAULT_RESPONSE_PRODUCT_SIMPLE_DTO =
        ResponseProductSimpleDto
            .builder()
            .productList(DEFAULT_FIND_SEARCH_PRODUCT_LIST)
            .pageData(LAST_POPULAR_PAGE_DATA)
            .size(10).build();

}
