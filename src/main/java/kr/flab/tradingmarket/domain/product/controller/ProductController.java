package kr.flab.tradingmarket.domain.product.controller;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.common.annotation.AuthCheck;
import kr.flab.tradingmarket.common.annotation.CurrentSession;
import kr.flab.tradingmarket.common.annotation.ValidImage;
import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.entity.ProductImage;
import kr.flab.tradingmarket.domain.product.exception.ProductRegisterException;
import kr.flab.tradingmarket.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;

    @PostMapping
    @AuthCheck
    public ResponseEntity<ResponseMessage> registerProduct(
        @Valid @RequestPart("data") RegisterProductDto registerProductDto,
        @ValidImage @RequestPart(value = "images") List<MultipartFile> images,
        @CurrentSession Long userNo) {

        List<ProductImage> productImages = imageService.uploadProductImages(images,
            ImageType.PRODUCT);
        try {
            productService.registerProduct(registerProductDto, productImages, userNo);
        } catch (Exception e) {
            imageService.deleteProductImages(productImages);
            throw new ProductRegisterException("Product Registration Failed", e);
        }
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }

}
