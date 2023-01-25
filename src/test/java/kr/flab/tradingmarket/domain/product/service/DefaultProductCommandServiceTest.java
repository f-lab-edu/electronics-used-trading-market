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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;

import kr.flab.tradingmarket.common.exception.DtoValidationException;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
import kr.flab.tradingmarket.domain.product.exception.ProductModifyException;
import kr.flab.tradingmarket.domain.product.exception.ProductRegisterException;

@ExtendWith(MockitoExtension.class)
class DefaultProductCommandServiceTest {

    @InjectMocks
    DefaultProductCommandService productCommandService;
    @Mock
    ProductService productService;

    @Mock
    ImageService imageService;

    @Test
    @DisplayName("service : 물품등록 : 성공")
    void successfulRegisterProduct() {
        //given
        //when
        productCommandService.registerProduct(DEFAULT_REGISTER_PRODUCT_DTO, DEFAULT_PRODUCT_MULTIPART_IMAGES,
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
        assertThatThrownBy(() -> productCommandService.registerProduct(DEFAULT_REGISTER_PRODUCT_DTO,
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
        assertThatThrownBy(() -> productCommandService.registerProduct(DEFAULT_REGISTER_PRODUCT_DTO,
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

    @Test
    @DisplayName("service : 물품수정 : DataAccessException 발생 실패")
    void failModifyProductByDataAccessException() {
        //given
        willThrow(new DataAccessException("test") {
        })
            .given(productService)
            .modifyProduct(any(), any(), any());

        //when
        //then
        assertThatThrownBy(() -> productCommandService.modifyProduct(1L, DEFAULT_REQUEST_MODIFY_PRODUCT_DTO,
            DEFAULT_PRODUCT_MULTIPART_IMAGES))
            .isInstanceOf(ProductModifyException.class);

        then(imageService)
            .should()
            .uploadProductImages(any(), any());
    }

    @Test
    @DisplayName("service : 물품수정 : DtoValidationException 발생 실패")
    void failModifyProductByDtoValidationException() {
        //given
        willThrow(DtoValidationException.class)
            .given(productService)
            .modifyProduct(any(), any(), any());

        //when
        //then
        assertThatThrownBy(() -> productCommandService.modifyProduct(1L, DEFAULT_REQUEST_MODIFY_PRODUCT_DTO,
            DEFAULT_PRODUCT_MULTIPART_IMAGES))
            .isInstanceOf(DtoValidationException.class);

        then(imageService)
            .should()
            .uploadProductImages(any(), any());
    }

    @Test
    @DisplayName("service : 물품수정 : ImageUploadException 발생 실패")
    void failModifyProductByImageUploadException() {
        //given
        willThrow(ImageUploadException.class)
            .given(imageService)
            .uploadProductImages(any(), any());
        //when
        //then
        assertThatThrownBy(() -> productCommandService.modifyProduct(1L, DEFAULT_REQUEST_MODIFY_PRODUCT_DTO,
            DEFAULT_PRODUCT_MULTIPART_IMAGES))
            .isInstanceOf(ImageUploadException.class);

        then(productService)
            .should(never())
            .modifyProduct(any(), any(), any());
        then(imageService)
            .should(never())
            .deleteProductImages(any());
    }

    @Test
    @DisplayName("service : 물품수정 : 이미지 업로드 있을때 성공")
    void successfulModifyProductByImageExistence() {
        //given
        given(imageService.uploadProductImages(any(), any()))
            .willReturn(DEFAULT_PRODUCT_IMAGES);

        //when
        productCommandService.modifyProduct(1L, DEFAULT_REQUEST_MODIFY_PRODUCT_DTO, DEFAULT_PRODUCT_MULTIPART_IMAGES);

        //then
        then(imageService)
            .should()
            .uploadProductImages(any(), any());
    }

    @Test
    @DisplayName("service : 물품수정 : 이미지 업로드 없을때 성공")
    void successfulModifyProductByNoImage() {
        //given
        //when
        productCommandService.modifyProduct(1L, DEFAULT_REQUEST_MODIFY_PRODUCT_DTO, EMPTY_PRODUCT_MULTIPART_IMAGES);

        //then
        then(imageService)
            .should(never())
            .uploadProductImages(any(), any());
    }

    @Test
    @DisplayName("service : 물품 수정시 조회 : 성공")
    void successfulFindByModifyProduct() {
        //given
        given(productService.findByModifyProduct(any()))
            .willReturn(DEFAULT_RESPONSE_MODIFY_PRODUCT_DTO);

        //when
        ResponseModifyProductDto result = productCommandService.findByModifyProduct(1L);

        //then
        assertThat(result).isEqualTo(DEFAULT_RESPONSE_MODIFY_PRODUCT_DTO);
    }

}
