package kr.flab.tradingmarket.domain.product.service;

import static kr.flab.tradingmarket.domain.product.config.ProductSearchCommonFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductSimpleDto;
import kr.flab.tradingmarket.domain.product.mapper.ProductMapper;

@ExtendWith(MockitoExtension.class)
class FullTextSearchServiceTest {

    @InjectMocks
    FullTextSearchService fullTextSearchService;

    @Mock
    ProductMapper productMapper;

    @Test
    @DisplayName("service : 물품 검색 : 인기순 검색 성공")
    void successfulSearchByPopular() {
        //given
        given(productMapper.findByProductsWithFullTextSearch(any(), any()))
            .willReturn(DEFAULT_FIND_SEARCH_PRODUCT_LIST);

        //when
        ResponseProductSimpleDto search = fullTextSearchService.search(DEFAULT_PAGE_POPULAR_PRODUCT_SEARCH_DTO, 1L);

        //then
        then(productMapper).should().findByProductsWithFullTextSearch(any(), any());

        assertThat(search.getProductList()).isEqualTo(DEFAULT_FIND_SEARCH_PRODUCT_LIST);
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getPageData()).isEqualTo(LAST_POPULAR_PAGE_DATA);
    }

    @Test
    @DisplayName("service : 물품 검색 : 날짜순 검색 성공")
    void successfulSearchByDate() {
        //given
        given(productMapper.findByProductsWithFullTextSearch(any(), any()))
            .willReturn(DEFAULT_FIND_SEARCH_PRODUCT_LIST);

        //when
        ResponseProductSimpleDto search = fullTextSearchService.search(DEFAULT_PAGE_DATE_PRODUCT_SEARCH_DTO, 1L);

        //then
        then(productMapper).should().findByProductsWithFullTextSearch(any(), any());

        assertThat(search.getProductList()).isEqualTo(DEFAULT_FIND_SEARCH_PRODUCT_LIST);
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getPageData()).isEqualTo(LAST_DATE_PAGE_DATA);

    }

    @Test
    @DisplayName("service : 물품 검색 : 가격 높은순 검색 성공")
    void successfulSearchByPriceDesc() {
        //given
        given(productMapper.findByProductsWithFullTextSearch(any(), any()))
            .willReturn(DEFAULT_FIND_SEARCH_PRODUCT_LIST);

        //when
        ResponseProductSimpleDto search = fullTextSearchService.search(DEFAULT_PAGE_PRICE_DESC_PRODUCT_SEARCH_DTO, 1L);

        //then
        then(productMapper).should().findByProductsWithFullTextSearch(any(), any());

        assertThat(search.getProductList()).isEqualTo(DEFAULT_FIND_SEARCH_PRODUCT_LIST);
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getPageData()).isEqualTo(LAST_PRICE_DESC_PAGE_DATA);

    }

    @Test
    @DisplayName("service : 물품 검색 : 가격 낮은순 검색 성공")
    void successfulSearchByPriceAsc() {
        //given
        given(productMapper.findByProductsWithFullTextSearch(any(), any()))
            .willReturn(DEFAULT_FIND_SEARCH_PRODUCT_LIST);

        //when
        ResponseProductSimpleDto search = fullTextSearchService.search(DEFAULT_PAGE_PRICE_ASC_PRODUCT_SEARCH_DTO, 1L);

        //then
        then(productMapper).should().findByProductsWithFullTextSearch(any(), any());

        assertThat(search.getProductList()).isEqualTo(DEFAULT_FIND_SEARCH_PRODUCT_LIST);
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getPageData()).isEqualTo(LAST_PRICE_ASC_PAGE_DATA);

    }

    @Test
    @DisplayName("service : 물품 검색 : 검색 결과가 없는 경우 성공")
    void successfulNoSearchResults() {
        //given
        given(productMapper.findByProductsWithFullTextSearch(any(), any()))
            .willReturn(new ArrayList<>());

        //when
        ResponseProductSimpleDto search = fullTextSearchService.search(DEFAULT_PAGE_PRICE_ASC_PRODUCT_SEARCH_DTO, 1L);

        //then
        then(productMapper).should().findByProductsWithFullTextSearch(any(), any());

        assertThat(search.getProductList()).isEqualTo(new ArrayList<>());
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getPageData()).isEqualTo(null);

    }

}