package kr.flab.tradingmarket.domain.product.controller;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.flab.tradingmarket.common.annotation.CurrentSession;
import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestLatestProductDto;
import kr.flab.tradingmarket.domain.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/products/search")
public class ProductSearchController {

    private final ProductSearchService elasticSearchService;

    private final ProductSearchService fullTextSearchService;

    private final ProductSearchService likeSearchService;

    @GetMapping(produces = "application/vnd.mymarket.appv1+json")
    public ResponseEntity<ResponseMessage> likeSearchVersion(@Valid ProductSearchDto productSearchDto,
        @CurrentSession Long userNo) {

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(likeSearchService.search(productSearchDto, userNo))
                .build());
    }

    @GetMapping(produces = "application/vnd.mymarket.appv2+json")
    public ResponseEntity<ResponseMessage> fullTextSearchVersion(@Valid ProductSearchDto productSearchDto,
        @CurrentSession Long userNo) {

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(fullTextSearchService.search(productSearchDto, userNo))
                .build());
    }

    @GetMapping(produces = "application/vnd.mymarket.appv3+json")
    public ResponseEntity<ResponseMessage> elasticSearchVersion(@Valid ProductSearchDto productSearchDto,
        @CurrentSession Long userNo) {

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(elasticSearchService.search(productSearchDto, userNo))
                .build());
    }

    @GetMapping("/last")
    public ResponseEntity<ResponseMessage> getProducts(@Valid RequestLatestProductDto requestLatestProductDto,
        @CurrentSession Long userNo) {

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(elasticSearchService.searchLatestProduct(requestLatestProductDto, userNo))
                .build());
    }

}
