package kr.flab.tradingmarket.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.image.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


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
    public void deleteImage(String deleteImageName) {
        s3Client.deleteObject(bucket, deleteImageName);
    }


}
