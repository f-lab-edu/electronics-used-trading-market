package kr.flab.tradingmarket.domain.product.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.mock.web.MockMultipartFile;

import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;

public class ProductControllerFixture {
    public static final String DEFAULT_PRODUCT_URL = "/products";

    public static final MockMultipartFile NO_IMAGE = new MockMultipartFile("images", "noImage", "image/*",
        "".getBytes());

    public static final RegisterProductDto FAIL_VALIDATION_PRODUCT_STATUS = RegisterProductDto.builder()
        .productName("무선마우스팔아요")
        .productCategoryNo(1L)
        .productAsExpirationDate(LocalDate.of(2022, 11, 29))
        .productStatus("FAIL")
        .productStock(1)
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.of(2021, 11, 20))
        .productPrice(BigDecimal.valueOf(10000))
        .productContent("무선마우스 싸게 팔아요 !!")
        .build();

    public static final RegisterProductDto FAIL_VALIDATION_PRODUCT_EXCHANGE_STATUS = RegisterProductDto.builder()
        .productName("무선마우스팔아요")
        .productCategoryNo(1L)
        .productAsExpirationDate(LocalDate.of(2022, 11, 29))
        .productStatus("HIGH")
        .productStock(1)
        .productExchangeStatus("FAIL")
        .purchaseDate(LocalDate.of(2021, 11, 20))
        .productPrice(BigDecimal.valueOf(10000))
        .productContent("무선마우스 싸게 팔아요 !!")
        .build();

}
