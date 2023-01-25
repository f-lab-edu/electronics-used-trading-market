package kr.flab.tradingmarket.domain.product.controller;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.common.annotation.AuthCheck;
import kr.flab.tradingmarket.common.annotation.CurrentSession;
import kr.flab.tradingmarket.common.annotation.ProductAuthCheck;
import kr.flab.tradingmarket.common.annotation.ValidImage;
import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.common.code.ValidationType;
import kr.flab.tradingmarket.domain.product.dto.request.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
import kr.flab.tradingmarket.domain.product.service.ProductCommandService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/products")
public class ProductController {

    private final ProductCommandService productCommandService;

    @PostMapping
    @AuthCheck
    public ResponseEntity<ResponseMessage> registerProduct(
        @Valid @RequestPart("data") RegisterProductDto registerProductDto,
        @ValidImage @RequestPart(value = "images") List<MultipartFile> images,
        @CurrentSession Long userNo) {

        productCommandService.registerProduct(registerProductDto, images, userNo);

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }

    @GetMapping("/edit/{productNo}")
    @AuthCheck
    @ProductAuthCheck
    public ResponseEntity<ResponseMessage> getModifyProduct(@PathVariable Long productNo) {
        ResponseModifyProductDto byProduct = productCommandService.findByModifyProduct(productNo);
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(byProduct)
                .build());
    }

    @PutMapping("/edit/{productNo}")
    @AuthCheck
    @ProductAuthCheck
    public ResponseEntity<ResponseMessage> modifyProduct(
        @PathVariable Long productNo,
        @Valid @RequestPart("data") RequestModifyProductDto modifyProduct,
        @ValidImage(type = ValidationType.UPDATE) @RequestPart(value = "images") List<MultipartFile> images) {

        productCommandService.modifyProduct(productNo, modifyProduct, images);

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(modifyProduct)
                .build());
    }

    @DeleteMapping("/{productNo}")
    @AuthCheck
    @ProductAuthCheck
    public ResponseEntity<ResponseMessage> deleteProduct(
        @PathVariable Long productNo) {

        productCommandService.deleteProduct(productNo);

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }
}
