package kr.flab.tradingmarket.domain.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.common.exception.DtoValidationException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.product.dto.request.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductDetailDto;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.product.exception.ProductModifyException;
import kr.flab.tradingmarket.domain.product.exception.ProductRegisterException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultProductCommandService implements ProductCommandService {

    private final ProductService productService;
    private final ImageService imageService;

    @Override
    public void registerProduct(RegisterProductDto registerProductDto, List<MultipartFile> images, Long userNo) {
        List<ProductImage> productImages = imageService.uploadProductImages(images, ImageType.PRODUCT,
            ProductImage.class);
        try {
            productService.registerProduct(registerProductDto, productImages, userNo);
        } catch (DataAccessException e) {
            imageService.deleteProductImages(productImages);
            throw new ProductRegisterException("Product Registration Failed", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseModifyProductDto findByModifyProduct(Long productNo) {
        return productService.findByModifyProduct(productNo);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseProductDetailDto findByDetailProduct(Long productNo) {
        return productService.findByDetailProduct(productNo);
    }

    @Override
    public void modifyProduct(Long productNo, RequestModifyProductDto modifyProduct,
        List<MultipartFile> images) {
        List<ProductImage> updateImageList = null;
        boolean hasUploadImage = !images.get(0).isEmpty();
        if (hasUploadImage) {
            updateImageList = imageService.uploadProductImages(images, ImageType.PRODUCT, ProductImage.class);
        }
        try {
            List<ProductImage> deleteImageList = productService.modifyProduct(productNo, modifyProduct,
                updateImageList);
            if (deleteImageList != null) {
                imageService.deleteProductImages(deleteImageList);
            }
        } catch (DataAccessException e) {
            if (hasUploadImage) {
                imageService.deleteProductImages(updateImageList);
            }
            throw new ProductModifyException("Product Modify Failed", e);
        } catch (DtoValidationException e) {
            if (hasUploadImage) {
                imageService.deleteProductImages(updateImageList);
            }
            throw e;
        }
    }

    @Override
    public void deleteProduct(Long productNo) {
        Optional<List<ProductImage>> productImages = productService.deleteProduct(productNo);
        productImages.ifPresent(imageService::deleteProductImages);
    }
}
