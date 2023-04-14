package kr.flab.tradingmarket.domain.category.service;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.flab.tradingmarket.domain.category.mapper.CategoryMapper;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    ProductCategoryService categoryService;
    @Mock
    CategoryMapper categoryMapper;

    @Test
    @DisplayName("service : 1차 카테고리 조회 : 성공")
    void successfulFindAllFirstCategories() {
        //given
        //when
        categoryService.findAllFirstCategories();

        //then
        then(categoryMapper)
            .should()
            .findFirstCategories();
    }

    @Test
    @DisplayName("service : 2차 카테고리 조회 : 성공")
    void successfulFindAllSecondCategories() {
        //given
        //when
        categoryService.findAllSecondCategories();

        //then
        then(categoryMapper)
            .should()
            .findSecondCategories();
    }

    @Test
    @DisplayName("service : 3차 카테고리 조회 : 성공")
    void successfulFindAllThirdCategories() {
        //given
        //when
        categoryService.findAllThirdCategories();

        //then
        then(categoryMapper)
            .should()
            .findThirdCategories();
    }

}