package kr.flab.tradingmarket.domain.image.service;

import kr.flab.tradingmarket.domain.image.utils.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String uploadImage(MultipartFile file, String uuidFileName, ImageType imageType) throws IOException;

    void deleteImage(String deleteImageName);

}
