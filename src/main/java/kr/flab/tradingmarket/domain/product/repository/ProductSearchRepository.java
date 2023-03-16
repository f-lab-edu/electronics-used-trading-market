package kr.flab.tradingmarket.domain.product.repository;

import java.util.List;

import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestLatestProductDto;
import kr.flab.tradingmarket.domain.product.entity.ProductSearch;

public interface ProductSearchRepository {
    List<ProductSearch> searchProduct(ProductSearchDto productSearchDto);

    List<ProductSearch> searchLatestProduct(RequestLatestProductDto requestLatestProductDto);

}
