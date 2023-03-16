package kr.flab.tradingmarket.domain.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.response.ProductSimpleDto;
import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;

@Mapper
@Repository
public interface ProductMapper {
    void insertProduct(Product user);

    void insertProductImages(List<ProductImage> productImages);

    void updateProductThumbnail(@Param("productThumbnail") ProductImage productThumbnail,
        @Param("productNo") Long productNo);

    int existsByProductNoAndSellerNo(@Param("productNo") Long productNo, @Param("sellerNo") Long sellerNo);

    Product findByThumbnailAndImages(Long productNo);

    Product findByImagesAndCategoryAndUserAndLikes(Long productNo);

    void updateProduct(Product from);

    void deleteProductImages(List<Long> removeImages);

    List<ProductImage> findProductImageByProductNo(Long productNo);

    void deleteProductImageByProductNo(Long productNo);

    void deleteProductByProductNo(Long productNo);

    List<ProductSimpleDto> findByProductsWithLikeSearch(@Param("searchDto") ProductSearchDto searchDto,
        @Param("userNo") Long userNo);

    List<ProductSimpleDto> findByProductsWithFullTextSearch(@Param("searchDto") ProductSearchDto searchDto,
        @Param("userNo") Long userNo);

    List<Product> findByNoList(List<Long> productNoList);

    Product findById(Long productNo);
}
