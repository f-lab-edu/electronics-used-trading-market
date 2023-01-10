package kr.flab.tradingmarket.domain.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;

public interface ImageService {

    String uploadImage(MultipartFile file, ImageType imageType);

    List<ProductImage> uploadProductImages(List<MultipartFile> files, ImageType imageType);

    void deleteImage(String deleteImageName);

    void deleteProductImages(List<ProductImage> deleteImageNames);
}
