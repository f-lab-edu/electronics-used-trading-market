package kr.flab.tradingmarket.domain.image.utils;

import kr.flab.tradingmarket.domain.image.exception.ExtensionNotSupportedException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.UUID;

public class ImageUtils {

    public static void validationImageExtension(MultipartFile file) {
        String extension = getExtension(file);
        boolean isMatch = Arrays.stream(ImageExtension.values())
                .anyMatch(ext -> ext.getName().equals(extension));
        if (!isMatch) {
            throw new ExtensionNotSupportedException(extension + " extension is not supported", extension);
        }
    }

    public static String getImageName() {
        return UUID.randomUUID().toString();
    }

    public static String getImagePath(MultipartFile file, String filename, ImageType imageType) {
        //File.separator 파일시스템에 따라서 구분자를 바꿔주는 ncloud object storage 경로 구분하는 /로 통일되어 있기 때문에 사용하지않음.
        return imageType.getType() + "/" + filename + "." + getExtension(file);
    }

    public static String separateImagePath(String path) {
        String[] split = path.split("/");
        return split[split.length - 2] + "/" + split[split.length - 1];
    }


    private static String getExtension(MultipartFile file) {
        return StringUtils.getFilenameExtension(file.getOriginalFilename());
    }

}
