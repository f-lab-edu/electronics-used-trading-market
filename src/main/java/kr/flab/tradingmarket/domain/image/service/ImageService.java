package kr.flab.tradingmarket.domain.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.image.entity.BaseImage;
import kr.flab.tradingmarket.domain.image.utils.ImageType;

public interface ImageService {

    String uploadImage(MultipartFile file, ImageType imageType);

    <T extends BaseImage> List<T> uploadProductImages(List<MultipartFile> files, ImageType imageType,
        Class<T> clazz);

    <T extends BaseImage> void deleteImage(T deleteImage);

    <T extends BaseImage> void deleteProductImages(List<T> deleteImages);
}
