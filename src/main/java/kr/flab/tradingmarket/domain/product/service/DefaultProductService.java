package kr.flab.tradingmarket.domain.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductMapper productMapper;

    @Override
    @Transactional
    public void registerProduct(RegisterProductDto registerProductDto, List<ProductImage> productImages, Long userNo) {
        Product convertProduct = Product.of(registerProductDto, userNo);
        productMapper.insertProduct(convertProduct);
        Long productNo = convertProduct.getProductNo();
        productImages.forEach(i -> i.setProductNo(productNo));
        productMapper.insertProductImages(productImages);
        productMapper.updateProductThumbnail(productImages.get(0), productNo);
    }

}
