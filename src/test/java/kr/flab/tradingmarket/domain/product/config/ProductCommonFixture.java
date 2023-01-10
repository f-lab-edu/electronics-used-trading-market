package kr.flab.tradingmarket.domain.product.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.category.entity.Category;
import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.entity.ProductExchangeStatus;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.product.entity.ProductStatus;
import kr.flab.tradingmarket.domain.user.entity.User;

public class ProductCommonFixture {

    public static final RegisterProductDto DEFAULT_REGISTER_PRODUCT_DTO = RegisterProductDto.builder()
        .productName("무선마우스팔아요")
        .productCategoryNo(1L)
        .productAsExpirationDate(LocalDate.of(2022, 11, 29))
        .productStatus("HIGH")
        .productStock(1)
        .productExchangeStatus("Y")
        .purchaseDate(LocalDate.of(2021, 11, 20))
        .productPrice(BigDecimal.valueOf(10000))
        .productContent("무선마우스 싸게 팔아요 !!")
        .build();

    public static final ProductImage DEFAULT_PRODUCT_THUMBNAILS = ProductImage.builder()
        .imageNo(1L)
        .originalFileName("ProductImage1.jpg")
        .fileLink("https://image1.kr.object.ncloudstorage.com/product/4242323-23124-53434-34343.jpg")
        .fileSize(0L)
        .productNo(1L)
        .build();

    public static final List<ProductImage> DEFAULT_REGISTERS_PRODUCT_IMAGES = List.of(
        DEFAULT_PRODUCT_THUMBNAILS
        , ProductImage.builder()
            .imageNo(2L)
            .originalFileName("ProductImage2.jpg")
            .fileLink("https://image1.kr.object.ncloudstorage.com/product/123123-123123-123123-123123.jpg")
            .fileSize(0L)
            .productNo(1L)
            .build()
        , ProductImage.builder()
            .imageNo(3L)
            .originalFileName("ProductImage3.jpg")
            .fileLink("https://image1.kr.object.ncloudstorage.com/product/42323-232342-32323-23232.jpg")
            .fileSize(0L)
            .productNo(1L)
            .build()
    );

    public static final Product DEFAULT_REGISTER_PRODUCT = Product.builder()
        .productName("무선마우스팔아요")
        .category(Category.builder().categoryNo(1L).build())
        .productAsExpirationDate(LocalDate.of(2022, 11, 29))
        .productStatus(ProductStatus.HIGH)
        .productStock(1)
        .productExchangeStatus(ProductExchangeStatus.Y)
        .purchaseDate(LocalDate.of(2021, 11, 20))
        .productPrice(BigDecimal.valueOf(10000))
        .productContent("무선마우스 싸게 팔아요 !!")
        .seller(User.builder().userNo(1L).build())
        .build();

    public static final MockMultipartFile REGISTER_PRODUCT_MULTIPART_1ST =
        new MockMultipartFile("images", "ProductImage1.jpg", MediaType.MULTIPART_FORM_DATA.toString(),
            "test".getBytes());
    public static final MockMultipartFile REGISTER_PRODUCT_MULTIPART_2ND =
        new MockMultipartFile("images", "ProductImage2.jpg", MediaType.MULTIPART_FORM_DATA.toString(),
            "test".getBytes());
    public static final MockMultipartFile REGISTER_PRODUCT_MULTIPART_3RD =
        new MockMultipartFile("images", "ProductImage3.jpg", MediaType.MULTIPART_FORM_DATA.toString(),
            "test".getBytes());
    public static final List<MultipartFile> DEFAULT_PRODUCT_MULTIPART_IMAGES = List.of(REGISTER_PRODUCT_MULTIPART_1ST,
        REGISTER_PRODUCT_MULTIPART_2ND,
        REGISTER_PRODUCT_MULTIPART_3RD);

    public static final ArgumentCaptor<Product> REGISTER_PRODUCT_CAPTURE = ArgumentCaptor.forClass(Product.class);
    public static final ArgumentCaptor<ProductImage> REGISTER_PRODUCT_IMAGE_CAPTURE = ArgumentCaptor.forClass(
        ProductImage.class);
    public static final ArgumentCaptor<List<ProductImage>> REGISTER_PRODUCT_IMAGE_LIST_CAPTURE = ArgumentCaptor.forClass(
        List.class);
}
