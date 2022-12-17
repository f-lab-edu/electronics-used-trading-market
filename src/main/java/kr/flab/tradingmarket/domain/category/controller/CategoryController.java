package kr.flab.tradingmarket.domain.category.controller;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.category.service.DefaultCategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final DefaultCategoryService categoryService;

    @GetMapping("/first")
    public ResponseEntity<ResponseMessage> firstCategoryList() {
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(categoryService.findAllFirstCategories())
                .build());
    }

    @GetMapping("/second")
    public ResponseEntity<ResponseMessage> secondCategoryList() {
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(categoryService.findAllSecondCategories())
                .build());
    }

    @GetMapping("/third")
    public ResponseEntity<ResponseMessage> thirdCategoryList() {
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(categoryService.findAllThirdCategories())
                .build());
    }
}
