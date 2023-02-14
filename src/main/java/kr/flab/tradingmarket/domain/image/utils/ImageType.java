package kr.flab.tradingmarket.domain.image.utils;

import kr.flab.tradingmarket.domain.image.service.ImageFactory;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;

public enum ImageType {

    PROFILE("profile", UserProfileImage.class,
        (originalFileName, fileLink, fileSize) -> UserProfileImage.builder()
            .originalFileName(originalFileName)
            .fileLink(fileLink)
            .fileSize(fileSize)
            .build()),
    PRODUCT("product", ProductImage.class,
        (originalFileName, fileLink, fileSize) -> ProductImage.builder()
            .originalFileName(originalFileName)
            .fileLink(fileLink)
            .fileSize(fileSize)
            .build());

    private final String type;
    private final ImageFactory factory;
    private final Class<?> clazz;

    ImageType(String type, Class<?> clazz, ImageFactory factory) {
        this.type = type;
        this.clazz = clazz;
        this.factory = factory;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ImageFactory getFactory() {
        return factory;
    }

    public String getType() {
        return type;
    }
}
