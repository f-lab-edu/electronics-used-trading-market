package kr.flab.tradingmarket.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.flab.tradingmarket.domain.category.dto.response.SimpleCategoryDto;
import kr.flab.tradingmarket.domain.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCategoryService implements CategoryService {
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<SimpleCategoryDto> findAllFirstCategories() {
        return categoryMapper.findFirstCategories();
    }

    @Transactional(readOnly = true)
    public List<SimpleCategoryDto> findAllSecondCategories() {
        return categoryMapper.findSecondCategories();
    }

    @Transactional(readOnly = true)
    public List<SimpleCategoryDto> findAllThirdCategories() {
        return categoryMapper.findThirdCategories();
    }

    @Transactional(readOnly = true)
    public boolean existCategory(Long categoryNo) {
        return categoryMapper.existsCategory(categoryNo) == 1;
    }
}
