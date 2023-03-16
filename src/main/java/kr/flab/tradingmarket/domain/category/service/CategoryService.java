package kr.flab.tradingmarket.domain.category.service;

import java.util.List;

import kr.flab.tradingmarket.domain.category.dto.response.SimpleCategoryDto;

public interface CategoryService {
    List<SimpleCategoryDto> findAllFirstCategories();

    List<SimpleCategoryDto> findAllSecondCategories();

    List<SimpleCategoryDto> findAllThirdCategories();

    boolean existCategory(Long categoryNo);
}
