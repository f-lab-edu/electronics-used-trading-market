package kr.flab.tradingmarket.domain.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.request.SearchOrder;
import kr.flab.tradingmarket.domain.product.dto.response.ProductSimpleDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductSimpleDto;
import kr.flab.tradingmarket.domain.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeSearchService implements ProductSearchService {

    private final ProductMapper productMapper;

    private Map<String, Object> getPageData(List<ProductSimpleDto> findSearch,
        ProductSearchDto productSearchDto) {

        if (findSearch.size() == 0) {
            return null;
        }

        Map<String, Object> pageData = new HashMap<>();

        ProductSimpleDto lastData = findSearch.get(findSearch.size() - 1);

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

    @Override
    public ResponseProductSimpleDto search(ProductSearchDto productSearchDto, Long userNo) {
        List<ProductSimpleDto> findSearch = productMapper.findByProductsWithLikeSearch(productSearchDto, userNo);
        return ResponseProductSimpleDto.builder()
            .productList(findSearch)
            .pageData(getPageData(findSearch, productSearchDto))
            .size(productSearchDto.getSize())
            .build();
    }
}
