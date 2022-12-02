package kr.flab.tradingmarket.domain.image.utils;

public enum ImageType {

    PROFILE("profile"),
    PRODUCT("product");

    private final String type;

    ImageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
