package kr.flab.tradingmarket.domain.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestLatestProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ProductSimpleDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductSimpleDto;
import kr.flab.tradingmarket.domain.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FullTextSearchService implements ProductSearchService {

    private final ProductMapper productMapper;

    @Override
    public ResponseProductSimpleDto search(ProductSearchDto productSearchDto, Long userNo) {
        List<ProductSimpleDto> findSearch = productMapper.findByProductsWithFullTextSearch(productSearchDto, userNo);
        return ResponseProductSimpleDto.builder()
            .productList(findSearch)
            .pageData(getPageData(findSearch, productSearchDto))
            .size(productSearchDto.getSize())
            .build();
    }

    @Override
    public ResponseProductSimpleDto searchLatestProduct(RequestLatestProductDto requestLatestProductDto, Long userNo) {
        //Todo 추후에 테스트용으로 구현
        return null;
    }
}
