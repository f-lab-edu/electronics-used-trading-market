package kr.flab.tradingmarket.domain.product.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import kr.flab.tradingmarket.domain.product.dto.request.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto;

public class ProductControllerFixture {
    public static final String DEFAULT_PRODUCT_URL = "/products";
    public static final String EDIT_PRODUCT_URL = "/products/edit/1";
    public static final RegisterProductDto FAIL_REGISTER_PRODUCT_DTO_VALIDATION_PRODUCT_STATUS = RegisterProductDto.builder()
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
    public static final RegisterProductDto FAIL_REGISTER_PRODUCT_DTO_VALIDATION_PRODUCT_EXCHANGE_STATUS = RegisterProductDto.builder()
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
    public static final RegisterProductDto FAIL_REGISTER_PRODUCT_DTO_CATEGORY_DOES_NOT_EXIST = RegisterProductDto.builder()
        .productName("무선마우스팔아요")
        .productCategoryNo(23232323L)
        .productAsExpirationDate(LocalDate.of(2022, 11, 29))
        .productStatus("HIGH")
        .productStock(1)
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.of(2021, 11, 20))
        .productPrice(BigDecimal.valueOf(10000))
        .productContent("무선마우스 싸게 팔아요 !!")
        .build();

    public static final RequestModifyProductDto FAIL_REQUEST_MODIFY_PRODUCT_DTO_CATEGORY_DOES_NOT_EXIST = RequestModifyProductDto.builder()
        .productName("무선마우스팔아요 수정")
        .productCategoryNo(99999999L)
        .productAsExpirationDate(LocalDate.of(2022, 11, 29))
        .productStatus("LOW")
        .productStock(1)
        .productSalesStatus("SALE")
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.of(2021, 11, 20))
        .productPrice(BigDecimal.valueOf(10000))
        .productContent("무선마우스 싸게 팔아요 !! 수정")
        .removeImageNoList(List.of(3L))
        .updateThumbnail(RequestModifyProductDto.UpdateImage.builder().updateType("OLD").imageNo(1L).build())
        .build();

    public static final RequestModifyProductDto FAIL_REQUEST_MODIFY_PRODUCT_DTO_VALIDATION_PRODUCT_SALES_STATUS = RequestModifyProductDto.builder()
        .productName("무선마우스팔아요 수정")
        .productCategoryNo(1L)
        .productAsExpirationDate(LocalDate.of(2022, 11, 29))
        .productStatus("LOW")
        .productStock(1)
        .productSalesStatus("FAIL")
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.of(2021, 11, 20))
        .productPrice(BigDecimal.valueOf(10000))
        .productContent("무선마우스 싸게 팔아요 !! 수정")
        .removeImageNoList(List.of(3L))
        .updateThumbnail(RequestModifyProductDto.UpdateImage.builder().updateType("OLD").imageNo(1L).build())
        .build();

    public static final RequestModifyProductDto FAIL_REQUEST_MODIFY_PRODUCT_DTO_VALIDATION_UPDATE_TYPE = RequestModifyProductDto.builder()
        .productName("무선마우스팔아요 수정")
        .productCategoryNo(1L)
        .productAsExpirationDate(LocalDate.of(2022, 11, 29))
        .productStatus("LOW")
        .productStock(1)
        .productSalesStatus("SALE")
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.of(2021, 11, 20))
        .productPrice(BigDecimal.valueOf(10000))
        .productContent("무선마우스 싸게 팔아요 !! 수정")
        .removeImageNoList(List.of(3L))
        .updateThumbnail(RequestModifyProductDto.UpdateImage.builder().updateType("ZZZ").imageNo(1L).build())
        .build();

    public static String productEditUrl(long productNo) {
        return EDIT_PRODUCT_URL + productNo;
    }

}
