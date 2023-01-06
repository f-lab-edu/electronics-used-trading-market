package kr.flab.tradingmarket.domain.product.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
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
}
