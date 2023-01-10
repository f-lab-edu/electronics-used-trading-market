package kr.flab.tradingmarket.domain.product.service;

import static kr.flab.tradingmarket.domain.product.config.ProductCommonFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.QueryTimeoutException;

import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.product.exception.ProductRegisterException;

@ExtendWith(MockitoExtension.class)
class DefaultProductCommandServiceTest {

    @InjectMocks
    DefaultProductCommandService defaultProductCommandService;
    @Mock
    ProductService productService;

    @Mock
    ImageService imageService;

    @Test
    @DisplayName("service : 물품등록 : 성공")
    void successfulRegisterProduct() {
        //given
        //when
        defaultProductCommandService.registerProduct(DEFAULT_REGISTER_PRODUCT_DTO, DEFAULT_PRODUCT_MULTIPART_IMAGES,
            1L);

        //then
        then(imageService)
            .should()
            .uploadProductImages(any(), any());

        then(productService)
            .should()
            .registerProduct(any(), any(), any());

        then(imageService)
            .should(never())
            .deleteProductImages(any());
    }

    @Test
    @DisplayName("service : 물품등록 : DB Exception 발생 실패")
    void failRegisterProductByDbException() {
        //given
        willThrow(QueryTimeoutException.class)
            .given(productService)
            .registerProduct(any(), any(), any());

        //when
        //then
        assertThatThrownBy(() -> defaultProductCommandService.registerProduct(DEFAULT_REGISTER_PRODUCT_DTO,
            DEFAULT_PRODUCT_MULTIPART_IMAGES,
            1L))
            .isInstanceOf(ProductRegisterException.class);

        then(imageService)
            .should()
            .uploadProductImages(any(), any());

        then(productService)
            .should()
            .registerProduct(any(), any(), any());

        then(imageService)
            .should()
            .deleteProductImages(any());
    }

    @Test
    @DisplayName("service : 물품등록 : ImageUploadException 발생 실패")
    void failRegisterProductByImageApiException() {
        //given
        willThrow(ImageUploadException.class)
            .given(imageService)
            .uploadProductImages(any(), any());

        //when
        //then
        assertThatThrownBy(() -> defaultProductCommandService.registerProduct(DEFAULT_REGISTER_PRODUCT_DTO,
            DEFAULT_PRODUCT_MULTIPART_IMAGES,
            1L))
            .isInstanceOf(ImageUploadException.class);

        then(imageService)
            .should()
            .uploadProductImages(any(), any());

        then(productService)
            .should(never())
            .registerProduct(any(), any(), any());

        then(imageService)
            .should(never())
            .deleteProductImages(any());
    }

}