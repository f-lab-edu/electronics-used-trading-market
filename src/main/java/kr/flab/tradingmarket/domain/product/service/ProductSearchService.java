package kr.flab.tradingmarket.domain.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.request.SearchOrder;
import kr.flab.tradingmarket.domain.product.dto.response.ProductSimpleDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductSimpleDto;

public interface ProductSearchService {

    ResponseProductSimpleDto search(ProductSearchDto productSearchDto, Long userNo);

    default Map<String, Object> getPageData(List<ProductSimpleDto> findSearch,
        ProductSearchDto productSearchDto) {

        int size = findSearch.size();

        if (size == 0) {
            return null;
        }

        Map<String, Object> pageData = new HashMap<>();

        ProductSimpleDto lastData = findSearch.get(size - 1);

        SearchOrder order = productSearchDto.getOrder();

        if (order == SearchOrder.PRICE_ASC || order == SearchOrder.PRICE_DESC) {
            pageData.put("lastPrice", lastData.getProductPrice());
        }

        if (order == SearchOrder.DATE) {
            pageData.put("lastModifiedDate", lastData.getModifyDate());
        }

        if (order == SearchOrder.POPULAR) {
            pageData.put("lastLikes", lastData.getLikeCount());
        }

        pageData.put("lastProductNo", lastData.getProductNo());
        pageData.put("order", order);

        return pageData;
    }

}
