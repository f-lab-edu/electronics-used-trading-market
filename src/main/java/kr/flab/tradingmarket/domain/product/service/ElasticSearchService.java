package kr.flab.tradingmarket.domain.product.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.flab.tradingmarket.domain.like.entity.Like;
import kr.flab.tradingmarket.domain.like.mapper.LikeMapper;
import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestLatestProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ProductSimpleDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductSimpleDto;
import kr.flab.tradingmarket.domain.product.entity.ProductSearch;
import kr.flab.tradingmarket.domain.product.repository.ElasticSearchRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ElasticSearchService implements ProductSearchService {

    private final LikeMapper likeMapper;
    private final ElasticSearchRepository productSearchRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseProductSimpleDto search(ProductSearchDto productSearchDto, Long userNo) {
        List<ProductSimpleDto> productList = convertToProductSimpleDtoList(userNo,
            productSearchRepository.searchProduct(productSearchDto));
        return createResponseProductSimpleDto(productList, productSearchDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseProductSimpleDto searchLatestProduct(RequestLatestProductDto requestLatestProductDto, Long userNo) {
        List<ProductSimpleDto> productList = convertToProductSimpleDtoList(userNo,
            productSearchRepository.searchLatestProduct(requestLatestProductDto));
        return ResponseProductSimpleDto.builder()
            .productList(productList)
            .pageData(getPageData(productList, requestLatestProductDto))
            .size(requestLatestProductDto.getSize())
            .build();
    }

    private List<ProductSimpleDto> convertToProductSimpleDtoList(Long userNo, List<ProductSearch> productSearch) {
        if (userNo == null) {
            return productSearch.stream()
                .map(i -> ProductSimpleDto.of(i, false))
                .collect(Collectors.toList());
        }
        List<Long> likeProductNoList = getMyLikes(userNo, productSearch);
        return productSearch.stream()
            .map(i -> ProductSimpleDto.of(i, likeProductNoList.contains(i.getProductNo())))
            .collect(Collectors.toList());
    }

    private List<Long> getMyLikes(Long userNo, List<ProductSearch> productSearch) {
        return likeMapper.findByUserNoAndProductNo(userNo,
                productSearch.stream().map(ProductSearch::getProductNo).toList())
            .stream().map(Like::getProductNo).toList();
    }

    private ResponseProductSimpleDto createResponseProductSimpleDto(List<ProductSimpleDto> productList,
        ProductSearchDto productSearchDto) {
        return ResponseProductSimpleDto.builder()
            .productList(productList)
            .pageData(getPageData(productList, productSearchDto))
            .size(productSearchDto.getSize())
            .build();
    }
}