package kr.flab.tradingmarket.domain.product.service;

import static kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto.*;
import static kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto.UpdateImage.UpdateType.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.flab.tradingmarket.common.exception.DtoValidationException;
import kr.flab.tradingmarket.domain.product.dto.request.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto.UpdateImage.UpdateType;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.product.mapper.ProductMapper;
import lombok.Getter;
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

    @Override
    public ResponseModifyProductDto findByModifyProduct(Long productNo) {
        Product product = productMapper.findByThumbnailAndImages(productNo);
        return ResponseModifyProductDto.from(product);
    }

    @Override
    public boolean isProductAuthorized(Long productNo, Long userNo) {
        return productMapper.existsByProductNoAndSellerNo(productNo, userNo) == 1;
    }

    @Transactional
    @Override
    public List<ProductImage> modifyProduct(Long productNo, RequestModifyProductDto modifyProduct,
        List<ProductImage> updateImageList) {

        ModifyProductValidObject validModifyProduct = new ModifyProductValidObject(productNo, modifyProduct,
            updateImageList, productMapper.findByThumbnailAndImages(productNo));
        productMapper.updateProduct(Product.of(modifyProduct, productNo));
        if (validModifyProduct.isModifyImage()) {
            return null;
        }
        updateImageAndThumbnail(validModifyProduct);
        return validModifyProduct.getCurrentDeleteImage();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void updateImageAndThumbnail(ModifyProductValidObject modify) {
        if (modify.isUpdateImage()) {
            List<ProductImage> productImages = modify.getUpdateImageList().get();
            productImages.forEach(i -> i.setProductNo(modify.getProductNo()));
            productMapper.insertProductImages(productImages);
        }
        if (modify.isThumbnailUpdate()) {
            productMapper.updateProductThumbnail(modify.getUpdateThumbnailImage().get(), modify.getProductNo());
        }
        if (modify.isDeleteImages()) {
            productMapper.deleteProductImages(modify.getRemoveImageNoList());
        }
    }

    @Override
    @Transactional
    public Optional<List<ProductImage>> deleteProduct(Long productNo) {
        Optional<List<ProductImage>> productImageList = Optional.ofNullable(
            productMapper.findProductImageByProductNo(productNo));
        productMapper.deleteProductImageByProductNo(productNo);
        productMapper.deleteProductByProductNo(productNo);
        return productImageList;
    }

    private static class ModifyProductValidObject {
        @Getter
        private final Long productNo;
        private final UpdateType thumbnailUpdateType;
        @Getter
        private final List<Long> removeImageNoList;
        @Getter
        private final Product currentProduct;
        private final List<ProductImage> currentProductImageList;
        @Getter
        private final Optional<List<ProductImage>> updateImageList;
        @Getter
        private final UpdateImage updateThumbnailInfo;
        @Getter
        private final Optional<ProductImage> updateThumbnailImage;
        @Getter
        private final List<ProductImage> currentDeleteImage;
        private final RequestModifyProductDto modifyProduct;

        private ModifyProductValidObject(Long productNo, RequestModifyProductDto modifyProduct,
            List<ProductImage> updateImageList, Product currentProduct) {
            this.currentProduct = currentProduct;
            this.updateImageList = Optional.ofNullable(updateImageList);
            this.productNo = productNo;
            this.modifyProduct = modifyProduct;
            this.updateThumbnailInfo = modifyProduct.getUpdateThumbnail();
            this.thumbnailUpdateType = UpdateType.valueOf(this.updateThumbnailInfo.getUpdateType());
            this.removeImageNoList = modifyProduct.getRemoveImageNoList();
            this.currentProductImageList = currentProduct.getImageList();
            validationImageCount();
            validationRemoveImages();
            this.updateThumbnailImage = Optional.ofNullable(validationThumbnail());
            this.currentDeleteImage = imageDeletionFilter();
        }

        private boolean isThumbnailUpdate() {
            return this.thumbnailUpdateType != NONE;
        }

        private boolean isUpdateImage() {
            return this.updateImageList.isPresent();
        }

        private boolean isDeleteImages() {
            return this.modifyProduct.getRemoveImageNoList().size() != 0;
        }

        private List<ProductImage> imageDeletionFilter() {
            return this.currentProductImageList.stream()
                .filter(i -> this.removeImageNoList.contains(i.getImageNo()))
                .toList();
        }

        private boolean isModifyImage() {
            return !isUpdateImage() && !isDeleteImages() && !this.isThumbnailUpdate();
        }

        private int sumTotalImageCount() {
            int currentImagesCount = this.currentProductImageList.size();
            int updateImagesCount = isUpdateImage() ? this.updateImageList.get().size() : 0;
            int removeImagesCount = this.removeImageNoList.size();
            return currentImagesCount + updateImagesCount - removeImagesCount;
        }

        private void validationImageCount() {
            int sumImageCount = sumTotalImageCount();
            if (sumImageCount < 1 || sumImageCount >= 11) {
                throw new DtoValidationException("images", "이미지는 1개 이상 10개 이하만 업로드 할 수있습니다.");
            }
        }

        private void validationRemoveImages() {
            if (this.removeImageNoList.size() != 0) {
                boolean hasImagePermission = new HashSet<>(
                    this.currentProductImageList.stream()
                        .map(ProductImage::getImageNo)
                        .toList())
                    .containsAll(this.removeImageNoList);
                if (!hasImagePermission) {
                    throw new DtoValidationException("removeImages", "잘못된 이미지 번호입니다.");
                }
            }
        }

        private ProductImage validationThumbnail() {
            if (this.thumbnailUpdateType == NONE) {
                if (this.removeImageNoList.size() != 0 && this.removeImageNoList
                    .contains(this.currentProduct.getProductThumbnail().getImageNo())) {
                    throw new DtoValidationException("updateThumbnail", "썸네일을 지정해주세요.");
                }
            }
            if (this.thumbnailUpdateType == UpdateType.NEW) {
                return this.updateImageList
                    .orElseThrow(() -> new DtoValidationException("images", "이미지를 업로드해주세요."))
                    .stream()
                    .filter(i -> i.getOriginalFileName().equals(this.updateThumbnailInfo.getImageName()))
                    .findFirst()
                    .orElseThrow(() -> new DtoValidationException("updateThumbnail", "존재하지 않는 이미지 입니다."));
            }
            if (this.thumbnailUpdateType == UpdateType.OLD) {
                if (this.removeImageNoList.contains(this.updateThumbnailInfo.getImageNo())) {
                    throw new DtoValidationException("updateThumbnail", "삭제할 이미지와 썸네일이 겹칩니다.");
                }
                return this.currentProduct.getImageList().stream()
                    .filter(i -> i.getImageNo().equals(this.updateThumbnailInfo.getImageNo()))
                    .findFirst()
                    .orElseThrow(() -> new DtoValidationException("updateThumbnail", "존재하지 않는 이미지 입니다."));
            }
            return null;
        }
    }

}
