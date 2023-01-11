package kr.flab.tradingmarket.domain.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;

@Mapper
@Repository
public interface ProductMapper {
    void insertProduct(Product user);

    void insertProductImages(List<ProductImage> productImages);

    void updateProductThumbnail(ProductImage productThumbnail, Long productNo);

    int existsByProductNoAndSellerNo(Long productNo, Long sellerNo);

    Product findByThumbnailAndImages(Long productNo);

    void updateProduct(Product from);

    void deleteProductImages(List<Long> removeImages);
}
