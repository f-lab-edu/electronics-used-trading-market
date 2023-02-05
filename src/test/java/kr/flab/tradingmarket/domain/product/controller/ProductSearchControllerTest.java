package kr.flab.tradingmarket.domain.product.controller;

import static kr.flab.tradingmarket.domain.product.config.ProductSearchCommonFixture.*;
import static kr.flab.tradingmarket.domain.product.controller.ProductSearchControllerFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.flab.tradingmarket.common.aop.LoginCheckAop;
import kr.flab.tradingmarket.domain.product.service.ProductSearchService;
import kr.flab.tradingmarket.domain.product.service.ProductService;
import kr.flab.tradingmarket.domain.user.service.LoginService;

@WebMvcTest(ProductSearchController.class)
@ExtendWith(MockitoExtension.class)
@Import({AopAutoConfiguration.class, LoginCheckAop.class})
@ActiveProfiles("test")
class ProductSearchControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    LoginService loginService;
    @MockBean
    ProductService productService;
    @MockBean(name = "likeSearchService")
    private ProductSearchService likeSearchService;
    @MockBean(name = "fullTextSearchService")
    private ProductSearchService fullTextSearchService;
    @MockBean(name = "elasticSearchService")
    private ProductSearchService elasticSearchService;

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    @DisplayName("controller : 상품 검색 Like : 성공")
    void successfulLikeSearch() throws Exception {
        //given
        given(likeSearchService.search(any(), any()))
            .willReturn(DEFAULT_RESPONSE_PRODUCT_SIMPLE_DTO);

        //when
        mockMvc.perform(get(PRODUCT_SEARCH_URL)
                .accept(VERSION1)
                .param("keyword", "test_search")
                .param("category", "1")
                .param("productAsExpirationDate", "2020-11-10")
                .param("productStatus", "HIGH")
                .param("productSalesStatus", "SALE")
                .param("productExchangeStatus", "Y")
                .param("purchaseDate", "2020-10-30")
                .param("maxPrice", "50000")
                .param("minPrice", "10000")
                .param("order", "POPULAR")
                .param("size", "10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.size").value(10))
            .andExpect(jsonPath("$.result.pageData.lastLikes").value(LAST_POPULAR_PAGE_DATA.get("lastLikes")))
            .andExpect(jsonPath("$.result.pageData.lastProductNo").value(LAST_POPULAR_PAGE_DATA.get("lastProductNo")))
            .andExpect(jsonPath("$.result.pageData.order").value(LAST_POPULAR_PAGE_DATA.get("order").toString()))
            .andExpect(jsonPath("$.result.productList.length()").value(10));

        //then
        then(likeSearchService)
            .should()
            .search(any(), any());
    }

    @Test
    @DisplayName("controller : 상품 검색 FullTextSearch : 성공")
    void successfulFullTextSearch() throws Exception {
        //given
        given(fullTextSearchService.search(any(), any()))
            .willReturn(DEFAULT_RESPONSE_PRODUCT_SIMPLE_DTO);

        //when
        mockMvc.perform(get(PRODUCT_SEARCH_URL)
                .accept(VERSION2)
                .param("keyword", "test_search")
                .param("category", "1")
                .param("productAsExpirationDate", "2020-11-10")
                .param("productStatus", "HIGH")
                .param("productSalesStatus", "SALE")
                .param("productExchangeStatus", "Y")
                .param("purchaseDate", "2020-10-30")
                .param("maxPrice", "50000")
                .param("minPrice", "10000")
                .param("order", "POPULAR")
                .param("size", "10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.size").value(10))
            .andExpect(jsonPath("$.result.pageData.lastLikes").value(LAST_POPULAR_PAGE_DATA.get("lastLikes")))
            .andExpect(jsonPath("$.result.pageData.lastProductNo").value(LAST_POPULAR_PAGE_DATA.get("lastProductNo")))
            .andExpect(jsonPath("$.result.pageData.order").value(LAST_POPULAR_PAGE_DATA.get("order").toString()))
            .andExpect(jsonPath("$.result.productList.length()").value(10));

        //then
        then(fullTextSearchService)
            .should()
            .search(any(), any());
    }

    @Test
    @DisplayName("controller : 상품 검색 Elasticsearch : 성공")
    void successfulElasticsearch() throws Exception {
        //given
        given(elasticSearchService.search(any(), any()))
            .willReturn(DEFAULT_RESPONSE_PRODUCT_SIMPLE_DTO);

        //when
        mockMvc.perform(get(PRODUCT_SEARCH_URL)
                .accept(VERSION3)
                .param("keyword", "test_search")
                .param("category", "1")
                .param("productAsExpirationDate", "2020-11-10")
                .param("productStatus", "HIGH")
                .param("productSalesStatus", "SALE")
                .param("productExchangeStatus", "Y")
                .param("purchaseDate", "2020-10-30")
                .param("maxPrice", "50000")
                .param("minPrice", "10000")
                .param("order", "POPULAR")
                .param("size", "10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.size").value(10))
            .andExpect(jsonPath("$.result.pageData.lastLikes").value(LAST_POPULAR_PAGE_DATA.get("lastLikes")))
            .andExpect(jsonPath("$.result.pageData.lastProductNo").value(LAST_POPULAR_PAGE_DATA.get("lastProductNo")))
            .andExpect(jsonPath("$.result.pageData.order").value(LAST_POPULAR_PAGE_DATA.get("order").toString()))
            .andExpect(jsonPath("$.result.productList.length()").value(10));

        //then
        then(elasticSearchService)
            .should()
            .search(any(), any());
    }

}
