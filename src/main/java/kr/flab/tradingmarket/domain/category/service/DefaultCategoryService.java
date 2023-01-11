package kr.flab.tradingmarket.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.flab.tradingmarket.domain.category.dto.response.SimpleCategoryDto;
import kr.flab.tradingmarket.domain.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {
    private final CategoryMapper categoryMapper;

    public List<SimpleCategoryDto> findAllFirstCategories() {
        return categoryMapper.findFirstCategories();
    }

    public List<SimpleCategoryDto> findAllSecondCategories() {
        return categoryMapper.findSecondCategories();
    }

    public List<SimpleCategoryDto> findAllThirdCategories() {
        return categoryMapper.findThirdCategories();
    }

    public boolean existCategory(Long categoryNo) {
        return categoryMapper.existsCategory(categoryNo) == 1;
    }
}
