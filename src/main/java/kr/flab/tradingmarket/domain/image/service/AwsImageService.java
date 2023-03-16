package kr.flab.tradingmarket.domain.image.service;

import static kr.flab.tradingmarket.domain.image.utils.ImageUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import kr.flab.tradingmarket.domain.image.entity.BaseImage;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.image.utils.ImageUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsImageService implements ImageService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadImage(MultipartFile file, ImageType imageType) {
        ImageUtils.validationImageExtension(file);
        String imagePath = ImageUtils.getImagePath(file, imageType);
        ObjectMetadata metadata = new ObjectMetadata();
        try {
            InputStream inputStream = file.getInputStream();
            metadata.setContentLength(inputStream.available());
            s3Client.putObject(new PutObjectRequest(bucket, imagePath, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ImageUploadException("Image upload failed", e);
        }
        return s3Client.getUrl(bucket, imagePath).toString();
    }

    @Override
    public <T extends BaseImage> List<T> uploadProductImages(List<MultipartFile> files, ImageType imageType,
        Class<T> clazz) {
        if (imageType.getClazz() != clazz) {
            throw new IllegalArgumentException("INVALID PARAMETER VALUE");
        }
        List<T> images = new ArrayList<>();
        for (MultipartFile file : files) {
            images.add(
                clazz.cast(imageType.getFactory()
                    .create(file.getOriginalFilename(), uploadImage(file, imageType), file.getSize())));
        }
        return images;
    }

    @Override
    public <T extends BaseImage> void deleteImage(T deleteImage) {
        s3Client.deleteObject(bucket, separateImagePath(deleteImage.getFileLink()));
    }

    @Override
    public <T extends BaseImage> void deleteProductImages(List<T> deleteImages) {
        for (BaseImage deleteImage : deleteImages) {
            deleteImage(deleteImage);
        }
    }

}
