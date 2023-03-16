package kr.flab.tradingmarket.domain.product.service;

import java.util.List;
import java.util.Optional;

import kr.flab.tradingmarket.domain.product.dto.request.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductDetailDto;
import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;

public interface ProductService {

    void registerProduct(RegisterProductDto registerProductDto, List<ProductImage> productImages, Long userNo);

    ResponseModifyProductDto findByModifyProduct(Long productNo);

    ResponseProductDetailDto findByDetailProduct(Long productNo);

    boolean isProductAuthorized(Long productNo, Long userNo);

    List<ProductImage> modifyProduct(Long productNo, RequestModifyProductDto modifyProduct,
        List<ProductImage> updateImageList);

    Optional<List<ProductImage>> deleteProduct(Long productNo);

    List<Product> findProductAndSellerByNoList(List<Long> productNoList);

    Product findById(Long productNo);

}
