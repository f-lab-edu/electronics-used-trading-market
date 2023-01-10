package kr.flab.tradingmarket.domain.product.service;

import static kr.flab.tradingmarket.domain.product.config.ProductCommonFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.product.mapper.ProductMapper;

@ExtendWith(MockitoExtension.class)
class DefaultProductServiceTest {

    @InjectMocks
    DefaultProductService productService;
    @Mock
    ProductMapper productMapper;

    @Test
    @DisplayName("service : 물품등록 : 성공")
    void successfulRegisterProduct() {
        //given

        //when
        productService.registerProduct(DEFAULT_REGISTER_PRODUCT_DTO, DEFAULT_REGISTERS_PRODUCT_IMAGES, 1L);

        //then
        then(productMapper)
            .should()
            .insertProduct(REGISTER_PRODUCT_CAPTURE.capture());
        assertThat(REGISTER_PRODUCT_CAPTURE.getValue()).isEqualTo(DEFAULT_REGISTER_PRODUCT);

        then(productMapper)
            .should()
            .insertProductImages(REGISTER_PRODUCT_IMAGE_LIST_CAPTURE.capture());
        List<ProductImage> images = REGISTER_PRODUCT_IMAGE_LIST_CAPTURE.getValue();
        assertThat(DEFAULT_REGISTERS_PRODUCT_IMAGES).containsExactly(
            images.get(0),
            images.get(1),
            images.get(2));

        then(productMapper)
            .should()
            .updateProductThumbnail(REGISTER_PRODUCT_IMAGE_CAPTURE.capture(), any());
        assertThat(REGISTER_PRODUCT_IMAGE_CAPTURE.getValue()).isEqualTo(
            DEFAULT_PRODUCT_THUMBNAILS);

    }
}