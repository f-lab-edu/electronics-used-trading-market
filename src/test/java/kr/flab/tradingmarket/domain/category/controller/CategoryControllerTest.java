package kr.flab.tradingmarket.domain.category.controller;

import static kr.flab.tradingmarket.domain.category.controller.CategoryControllerTestFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import kr.flab.tradingmarket.common.config.WebConfig;
import kr.flab.tradingmarket.common.interceptor.ProductAuthorizationInterceptor;
import kr.flab.tradingmarket.common.resolver.SessionResolver;
import kr.flab.tradingmarket.domain.category.service.CategoryService;

@WebMvcTest(controllers = CategoryController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, SessionResolver.class,
        ProductAuthorizationInterceptor.class})})
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CategoryControllerTest {

    @MockBean
    CategoryService categoryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("controller : 1차 카테고리 조회 : 성공")
    void successfulFirstCategoryList() throws Exception {
        //given
        //when
        mockMvc.perform(get(FIRST_CATEGORY_LIST_URL))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(categoryService)
            .should()
            .findAllFirstCategories();
    }

    @Test
    @DisplayName("controller : 2차 카테고리 조회 : 성공")
    void successfulSecondCategoryList() throws Exception {
        //given
        //when
        mockMvc.perform(get(SECOND_CATEGORY_LIST_URL))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(categoryService)
            .should()
            .findAllSecondCategories();
    }

    @Test
    @DisplayName("controller : 3차 카테고리 조회 : 성공")
    void successfulThirdCategoryList() throws Exception {
        //given
        //when
        mockMvc.perform(get(THIRD_CATEGORY_LIST_URL))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(categoryService)
            .should()
            .findAllThirdCategories();
    }

}