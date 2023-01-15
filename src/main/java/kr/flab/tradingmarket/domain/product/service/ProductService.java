package kr.flab.tradingmarket.domain.product.service;

import java.util.List;
import java.util.Optional;

import kr.flab.tradingmarket.domain.product.dto.request.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;

public interface ProductService {

    void registerProduct(RegisterProductDto registerProductDto, List<ProductImage> productImages, Long userNo);

    ResponseModifyProductDto findByModifyProduct(Long productNo);

    boolean isProductAuthorized(Long productNo, Long userNo);

    Optional<List<ProductImage>> modifyProduct(Long productNo, RequestModifyProductDto modifyProduct,
        List<ProductImage> updateImageList);

    Optional<List<ProductImage>> deleteProduct(Long productNo);
}
