package kr.flab.tradingmarket.domain.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.product.dto.request.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
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
        List<ProductImage> productImages = imageService.uploadProductImages(images,
            ImageType.PRODUCT);
        try {
            productService.registerProduct(registerProductDto, productImages, userNo);
        } catch (DataAccessException e) {
            imageService.deleteProductImages(productImages);
            throw new ProductRegisterException("Product Registration Failed", e);
        }
    }

    @Override
    public ResponseModifyProductDto findByModifyProduct(Long productNo) {
        return productService.findByModifyProduct(productNo);
    }

    @Override
    public void modifyProduct(Long productNo, RequestModifyProductDto modifyProduct,
        List<MultipartFile> images) {
        List<ProductImage> updateImageList = null;
        boolean hasUploadImage = !images.get(0).isEmpty();
        if (hasUploadImage) {
            updateImageList = imageService.uploadProductImages(images, ImageType.PRODUCT);
        }
        try {
            Optional<List<ProductImage>> productImages = productService.modifyProduct(productNo, modifyProduct,
                updateImageList);
            productImages.ifPresent(imageService::deleteProductImages);
        } catch (RuntimeException e) {
            if (hasUploadImage) {
                imageService.deleteProductImages(updateImageList);
            }
            throw new ProductModifyException("Product Modify Failed", e);
        }
    }
}
