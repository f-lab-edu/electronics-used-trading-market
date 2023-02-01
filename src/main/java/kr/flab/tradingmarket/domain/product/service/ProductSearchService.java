package kr.flab.tradingmarket.domain.product.service;

import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductSimpleDto;

public interface ProductSearchService {

    ResponseProductSimpleDto search(ProductSearchDto productSearchDto, Long userNo);

}
