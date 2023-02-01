package kr.flab.tradingmarket.domain.product.controller;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.flab.tradingmarket.common.annotation.AuthCheck;
import kr.flab.tradingmarket.common.annotation.CurrentSession;
import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/products/search")
public class ProductSearchController {

    private final ProductSearchService likeSearchService;

    @GetMapping(produces = "application/vnd.mymarket.appv1+json")
    @AuthCheck
    public ResponseEntity<ResponseMessage> likeSearchVersion(@Valid ProductSearchDto productSearchDto,
        @CurrentSession Long userNo) {

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(likeSearchService.search(productSearchDto, userNo))
                .build());
    }

}
