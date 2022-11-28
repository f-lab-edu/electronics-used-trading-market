package kr.flab.tradingmarket.domain.image.utils;

public enum ImageExtension {

    PNG("png"),
    JPEG("jpeg"),
    JPG("jpg");

    private final String name;

    ImageExtension(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
