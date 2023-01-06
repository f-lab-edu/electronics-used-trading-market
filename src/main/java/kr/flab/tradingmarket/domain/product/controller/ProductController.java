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
import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;
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

}
