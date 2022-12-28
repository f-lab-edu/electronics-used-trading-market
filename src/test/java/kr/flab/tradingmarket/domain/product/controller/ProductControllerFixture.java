package kr.flab.tradingmarket.domain.product.controller;

import org.springframework.mock.web.MockMultipartFile;

class ProductControllerFixture {
    public static final String DEFAULT_PRODUCT_URL = "/products";

    public static final MockMultipartFile NO_IMAGE = new MockMultipartFile("images", "noImage", "image/*",
        "".getBytes());
}
