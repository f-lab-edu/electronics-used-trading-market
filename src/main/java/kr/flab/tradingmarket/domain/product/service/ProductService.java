package kr.flab.tradingmarket.domain.product.service;

import java.util.List;

import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;

public interface ProductService {

    void registerProduct(RegisterProductDto registerProductDto, List<ProductImage> productImages, Long userNo);
}
