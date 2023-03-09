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

import kr.flab.tradingmarket.common.exception.DtoValidationException;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductDetailDto;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.product.exception.ProductNotFoundException;
import kr.flab.tradingmarket.domain.product.mapper.ProductMapper;
import kr.flab.tradingmarket.domain.product.repository.ElasticSearchDocumentRepository;

@ExtendWith(MockitoExtension.class)
class DefaultProductServiceTest {

    @InjectMocks
    DefaultProductService productService;
    @Mock
    ProductMapper productMapper;

    @Mock
    ElasticSearchDocumentRepository elasticSearchDocumentRepository;

    @Test
    @DisplayName("service : 물품등록 : 성공")
    void successfulRegisterProduct() {
        //given

        //when
        productService.registerProduct(DEFAULT_REGISTER_PRODUCT_DTO, DEFAULT_PRODUCT_IMAGES, 1L);

        //then
        then(productMapper)
            .should()
            .insertProduct(REGISTER_PRODUCT_CAPTURE.capture());
        assertThat(REGISTER_PRODUCT_CAPTURE.getValue()).isEqualTo(DEFAULT_REGISTER_PRODUCT);

        then(productMapper)
            .should()
            .insertProductImages(REGISTER_PRODUCT_IMAGE_LIST_CAPTURE.capture());
        List<ProductImage> images = REGISTER_PRODUCT_IMAGE_LIST_CAPTURE.getValue();
        assertThat(DEFAULT_PRODUCT_IMAGES).containsExactly(
            images.get(0),
            images.get(1),
            images.get(2));

        then(productMapper)
            .should()
            .updateProductThumbnail(REGISTER_PRODUCT_IMAGE_CAPTURE.capture(), any());
        assertThat(REGISTER_PRODUCT_IMAGE_CAPTURE.getValue()).isEqualTo(
            DEFAULT_PRODUCT_THUMBNAILS);

    }

    @Test
    @DisplayName("service : 수정할 물품 조회 : 성공")
    public void successfulFindByModifyProduct() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_THUMBNAIL_AND_IMAGES_PRODUCT);

        //when
        ResponseModifyProductDto result = productService.findByModifyProduct(any());

        //then
        then(productMapper)
            .should()
            .findByThumbnailAndImages(any());
        assertThat(result).usingRecursiveComparison().isEqualTo(DEFAULT_RESPONSE_MODIFY_PRODUCT_DTO);

    }

    @Test
    @DisplayName("service : 물품 수정 삭제에 대한 권한체크 : 성공")
    public void successfulIsProductAuthorized() {
        //given
        given(productMapper.existsByProductNoAndSellerNo(any(), any()))
            .willReturn(1);

        //when
        boolean productAuthorized = productService.isProductAuthorized(any(), any());

        //then
        then(productMapper)
            .should()
            .existsByProductNoAndSellerNo(any(), any());
        assertThat(productAuthorized).isEqualTo(true);
    }

    @Test
    @DisplayName("service : 물품 수정 삭제에 대한 권한체크 : 실패")
    public void failIsProductAuthorized() {
        //given
        given(productMapper.existsByProductNoAndSellerNo(any(), any()))
            .willReturn(0);

        //when
        boolean productAuthorized = productService.isProductAuthorized(any(), any());

        //then
        then(productMapper)
            .should()
            .existsByProductNoAndSellerNo(any(), any());
        assertThat(productAuthorized).isEqualTo(false);
    }

    @Test
    @DisplayName("service : 물품수정 validation : 썸네일 업데이트를 기존에 있던 이미지로 시도하는데 해당 이미지 이름이 기존 이미지 리스트에 없는경우 실패")
    public void failModifyDtoValidationByOldImage() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        DtoValidationException catchException = catchThrowableOfType(
            () -> productService.modifyProduct(any(), FAIL_OLD_IMAGE_UPDATE_REQUEST_MODIFY_PRODUCT_DTO,
                DEFAULT_PRODUCT_IMAGES), DtoValidationException.class);

        //then
        assertThat(catchException)
            .extracting("field", "message")
            .containsExactly("updateThumbnail", "존재하지 않는 이미지 입니다.");
    }

    @Test
    @DisplayName("service : 물품수정 validation : 썸네일 업데이트를 새로 추가한 이미지로 시도하는데 해당 이미지 이름이 추가된 이미지 리스트에 없는경우 실패")
    public void failModifyDtoValidationByNewImage() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        DtoValidationException catchException = catchThrowableOfType(
            () -> productService.modifyProduct(any(), FAIL_NEW_IMAGE_UPDATE_REQUEST_MODIFY_PRODUCT_DTO,
                DEFAULT_PRODUCT_IMAGES), DtoValidationException.class);

        //then
        assertThat(catchException)
            .extracting("field", "message")
            .containsExactly("updateThumbnail", "존재하지 않는 이미지 입니다.");
    }

    @Test
    @DisplayName("service : 물품수정 validation : 기존 썸네일 이미지를 삭제하고 새로운 썸네일 이미지를 지정하지 않았을때 실패")
    public void failModifyDtoValidationByDeleteImage() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        DtoValidationException catchException = catchThrowableOfType(
            () -> productService.modifyProduct(any(), FAIL_DELETE_IMAGE_REQUEST_MODIFY_PRODUCT_DTO,
                DEFAULT_PRODUCT_IMAGES), DtoValidationException.class);

        //then
        assertThat(catchException)
            .extracting("field", "message")
            .containsExactly("updateThumbnail", "썸네일을 지정해주세요.");
    }

    @Test
    @DisplayName("service : 물품수정 validation : 삭제할 이미지와 업데이트할 썸네일이 겹치는 경우 실패")
    public void failModifyDtoValidationByOverlappingThumbnailsAndDeletedImages() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        DtoValidationException catchException = catchThrowableOfType(
            () -> productService.modifyProduct(any(), FAIL_OVERLAPPING_THUMBNAILS_DELETED_IMAGES,
                null), DtoValidationException.class);

        //then
        assertThat(catchException)
            .extracting("field", "message")
            .containsExactly("updateThumbnail", "삭제할 이미지와 썸네일이 겹칩니다.");
    }

    @Test
    @DisplayName("service : 물품수정 validation : 총 이미지의 개수가 11개 이상이 될 때 실패")
    public void failModifyDtoValidationByTotalImageExceeded() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        DtoValidationException catchException = catchThrowableOfType(
            () -> productService.modifyProduct(any(), DEFAULT_REQUEST_MODIFY_PRODUCT_DTO,
                FULL_PRODUCT_IMAGES), DtoValidationException.class);

        //then
        assertThat(catchException)
            .extracting("field", "message")
            .containsExactly("images", "이미지는 1개 이상 10개 이하만 업로드 할 수있습니다.");
    }

    @Test
    @DisplayName("service : 물품수정 validation : 총 이미지의 개수가 1개 미만이 될 때 실패")
    public void failModifyDtoValidationByTotalImageUnder() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        DtoValidationException catchException = catchThrowableOfType(
            () -> productService.modifyProduct(any(), DELETE_ALL_IMAGES_REQUEST_MODIFY_PRODUCT_DTO,
                null), DtoValidationException.class);

        //then
        assertThat(catchException)
            .extracting("field", "message")
            .containsExactly("images", "이미지는 1개 이상 10개 이하만 업로드 할 수있습니다.");
    }

    @Test
    @DisplayName("service : 물품수정 validation : 권한이 없는 다른 Product Image를 삭제하려고 할 때 실패")
    public void failModifyDtoValidationByWrongImageNumber() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        DtoValidationException catchException = catchThrowableOfType(
            () -> productService.modifyProduct(any(), DELETE_WRONG_IMAGE_REQUEST_MODIFY_PRODUCT_DTO,
                DEFAULT_PRODUCT_IMAGES), DtoValidationException.class);

        //then
        assertThat(catchException)
            .extracting("field", "message")
            .containsExactly("removeImages", "잘못된 이미지 번호입니다.");
    }

    @Test
    @DisplayName("service : 물품수정 validation : 이미지 변경있을때 성공")
    public void successfulModifyDtoValidationByImageChange() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        productService.modifyProduct(any(), DEFAULT_REQUEST_MODIFY_PRODUCT_DTO, DEFAULT_PRODUCT_IMAGES);

        //then
        then(productMapper)
            .should()
            .insertProductImages(any());
        then(productMapper)
            .should()
            .updateProductThumbnail(any(), any());
        then(productMapper)
            .should()
            .updateProductThumbnail(any(), any());
    }

    @Test
    @DisplayName("service : 물품수정 validation : 이미지 변경없을때 성공")
    public void successfulModifyDtoValidationByNoImageChange() {
        //given
        given(productMapper.findByThumbnailAndImages(any()))
            .willReturn(DEFAULT_REGISTER_PRODUCT);

        //when
        productService.modifyProduct(any(), NO_CHANGE_IMAGE_REQUEST_MODIFY_PRODUCT_DTO, null);

        //then
        then(productMapper)
            .should(never())
            .insertProductImages(any());
        then(productMapper)
            .should(never())
            .updateProductThumbnail(any(), any());
        then(productMapper)
            .should(never())
            .updateProductThumbnail(any(), any());
    }

    @Test
    @DisplayName("service : 물품 삭제 : 성공")
    public void successfulDeleteProduct() {
        //given
        given(productMapper.findProductImageByProductNo(any()))
            .willReturn(DEFAULT_PRODUCT_IMAGES);

        //when
        productService.deleteProduct(1L);

        //then
        then(productMapper)
            .should()
            .findProductImageByProductNo(any());
        then(productMapper)
            .should()
            .deleteProductImageByProductNo(any());
        then(productMapper)
            .should()
            .deleteProductByProductNo(any());
        then(elasticSearchDocumentRepository)
            .should()
            .deleteById(any());
    }

    @Test
    @DisplayName("service : 물품 조회 : 성공")
    public void successfulFindByDetailProduct() {
        //given
        given(productMapper.findByImagesAndCategoryAndUserAndLikes(any()))
            .willReturn(DEFAULT_DETAILS_PRODUCT);

        //when
        ResponseProductDetailDto findProduct = productService.findByDetailProduct(1L);

        //then
        then(productMapper)
            .should()
            .findByImagesAndCategoryAndUserAndLikes(any());

        assertThat(findProduct).usingRecursiveComparison()
            .isEqualTo(DEFAULT_RESPONSE_PRODUCT_DETAIL_DTO);
    }

    @Test
    @DisplayName("service : 물품 조회 : 조회한 물품이 존재하지 않는 경우 실패 ")
    public void failFindByDetailProduct() {
        //given
        given(productMapper.findByImagesAndCategoryAndUserAndLikes(any()))
            .willReturn(null);

        //when
        assertThatThrownBy(() -> productService.
            findByDetailProduct(1L)).isInstanceOf(ProductNotFoundException.class);

        //then
        then(productMapper)
            .should()
            .findByImagesAndCategoryAndUserAndLikes(any());
    }

}