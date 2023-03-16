package kr.flab.tradingmarket.domain.product.service;

import java.util.List;

import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestLatestProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.SearchOrder;
import kr.flab.tradingmarket.domain.product.dto.response.ProductSimpleDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponsePageData;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductSimpleDto;

public interface ProductSearchService {

    ResponseProductSimpleDto search(ProductSearchDto productSearchDto, Long userNo);

    default ResponsePageData<?> getPageData(List<ProductSimpleDto> findSearch,
        ProductSearchDto productSearchDto) {

        int size = findSearch.size();

        if (size == 0) {
            return null;
        }

        ProductSimpleDto lastData = findSearch.get(size - 1);

        SearchOrder order = productSearchDto.getOrder();

        if (order == SearchOrder.PRICE_ASC || order == SearchOrder.PRICE_DESC) {
            return new ResponsePageData<>(lastData.getProductPrice(), order, lastData.getProductNo());
        }

        if (order == SearchOrder.DATE) {
            return new ResponsePageData<>(lastData.getModifyDate(), order, lastData.getProductNo());
        }

        if (order == SearchOrder.POPULAR) {
            return new ResponsePageData<>(lastData.getLikeCount(), order, lastData.getProductNo());
        }

        return null;
    }

    default ResponsePageData<?> getPageData(List<ProductSimpleDto> findSearch,
        RequestLatestProductDto requestLatestProductDto) {
        int size = findSearch.size();

        if (size == 0) {
            return null;
        }

        ProductSimpleDto lastData = findSearch.get(size - 1);

        return new ResponsePageData<>(lastData.getModifyDate(), SearchOrder.DATE, lastData.getProductNo());
    }

    ResponseProductSimpleDto searchLatestProduct(RequestLatestProductDto requestLatestProductDto, Long userNo);
}
