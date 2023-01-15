package kr.flab.tradingmarket.domain.product.controller;

import static kr.flab.tradingmarket.domain.product.config.ProductCommonFixture.*;
import static kr.flab.tradingmarket.domain.product.controller.ProductControllerFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.flab.tradingmarket.common.aop.LoginCheckAop;
import kr.flab.tradingmarket.common.exception.DtoValidationException;
import kr.flab.tradingmarket.common.validator.CategoryValidator;
import kr.flab.tradingmarket.domain.category.service.CategoryService;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.product.exception.ProductModifyException;
import kr.flab.tradingmarket.domain.product.exception.ProductRegisterException;
import kr.flab.tradingmarket.domain.product.service.ProductCommandService;
import kr.flab.tradingmarket.domain.product.service.ProductService;
import kr.flab.tradingmarket.domain.user.service.LoginService;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
@Import({AopAutoConfiguration.class, LoginCheckAop.class, CategoryValidator.class})
@ActiveProfiles("test")
class ProductControllerTest {

    @MockBean
    ProductCommandService productCommandService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    LoginService loginService;

    @MockBean
    ProductService productService;
    @MockBean
    CategoryService categoryService;

    @BeforeEach
    public void initMock() throws JsonProcessingException {
        ;
        given(categoryService.existCategory(any()))
            .willReturn(true);
    }

    private MockMultipartFile makeRequestPartFixture(Object object) throws JsonProcessingException {
        return new MockMultipartFile("data", "data",
            "application/json", toJson(object).getBytes(StandardCharsets.UTF_8));
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    @DisplayName("controller : 상품등록 테스트 : 성공")
    void successfulRegisterProduct() throws Exception {
        //given
        //when
        mockMvc.perform(multipart(HttpMethod.POST, DEFAULT_PRODUCT_URL).file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(makeRequestPartFixture(DEFAULT_REGISTER_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(productCommandService)
            .should()
            .registerProduct(any(), any(), any());

    }

    @Test
    @DisplayName("controller : 상품등록 테스트 : 이미지 개수초과로 인한 실패")
    void failRegisterProductByExceedingTheNumberOfFiles() throws Exception {
        //given
        //when
        mockMvc.perform(multipart(HttpMethod.POST, DEFAULT_PRODUCT_URL)
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(makeRequestPartFixture(DEFAULT_REGISTER_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect((rs) -> assertThat(rs.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
            .andExpect(status().isBadRequest());

        //then
        then(productCommandService)
            .should(never())
            .registerProduct(any(), any(), any());

    }

    @Test
    @DisplayName("controller : 상품등록 테스트 : 이미지가 없는경우 실패")
    void failRegisterProductByNoImage() throws Exception {
        //given
        //when
        mockMvc.perform(multipart(HttpMethod.POST, DEFAULT_PRODUCT_URL)
                .file(NO_IMAGE)
                .file(makeRequestPartFixture(DEFAULT_REGISTER_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect((rs) -> assertThat(rs.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
            .andExpect(status().isBadRequest());

        //then
        then(productCommandService)
            .should(never())
            .registerProduct(any(), any(), any());
    }

    @Test
    @DisplayName("controller : 상품등록 테스트 : 클라우드 이미지 업로드 실패")
    void failRegisterProductByS3Exception() throws Exception {
        //given
        willThrow(ImageUploadException.class)
            .given(productCommandService)
            .registerProduct(any(), any(), any());

        //when
        mockMvc.perform(multipart(HttpMethod.POST, DEFAULT_PRODUCT_URL)
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(makeRequestPartFixture(DEFAULT_REGISTER_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        //then
        then(productCommandService)
            .should()
            .registerProduct(any(), any(), any());
    }

    @Test
    @DisplayName("controller : 상품등록 테스트 : DB 상품등록 실패")
    void failRegisterProductByDbException() throws Exception {
        //given
        willThrow(ProductRegisterException.class)
            .given(productCommandService)
            .registerProduct(any(), any(), any());

        //when
        mockMvc.perform(multipart(HttpMethod.POST, DEFAULT_PRODUCT_URL)
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(makeRequestPartFixture(DEFAULT_REGISTER_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        //then
        then(productCommandService)
            .should()
            .registerProduct(any(), any(), any());
    }

    @Test
    @DisplayName("controller : 수정 상품 조회 테스트 : 성공")
    void successfulGetModifyProduct() throws Exception {
        //given
        given(productService.isProductAuthorized(any(), any()))
            .willReturn(true);

        //when
        mockMvc.perform(get(productEditUrl(1L)))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(productCommandService)
            .should()
            .findByModifyProduct(any());
    }

    @Test
    @DisplayName("controller : 수정 상품 조회 테스트 : 권한 없음 실패")
    void failGetModifyProductByNoPermission() throws Exception {
        //given
        given(productService.isProductAuthorized(any(), any()))
            .willReturn(false);

        //when
        mockMvc.perform(get(productEditUrl(1L)))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(productCommandService)
            .should(never())
            .findByModifyProduct(any());
    }

    @Test
    @DisplayName("controller : 상품 수정 테스트 : 성공")
    void successfulModifyProduct() throws Exception {
        //given
        given(productService.isProductAuthorized(any(), any()))
            .willReturn(true);

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, productEditUrl(1L))
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(makeRequestPartFixture(DEFAULT_REQUEST_MODIFY_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        then(productCommandService)
            .should()
            .modifyProduct(any(), any(), any());

    }

    @Test
    @DisplayName("controller : 상품 수정 테스트 : 권한 없음 실패")
    void failModifyProductByNoPermission() throws Exception {
        //given
        given(productService.isProductAuthorized(any(), any()))
            .willReturn(false);

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, productEditUrl(1L))
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(makeRequestPartFixture(DEFAULT_REQUEST_MODIFY_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized());

        //then
        then(productCommandService)
            .should(never())
            .modifyProduct(any(), any(), any());
    }

    @Test
    @DisplayName("controller : 상품 수정 테스트 : 상품 수정 실패")
    void failModifyProductByProductModifyException() throws Exception {
        //given
        given(productService.isProductAuthorized(any(), any()))
            .willReturn(true);
        willThrow(ProductModifyException.class)
            .given(productCommandService)
            .modifyProduct(any(), any(), any());

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, productEditUrl(1L))
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(makeRequestPartFixture(DEFAULT_REQUEST_MODIFY_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        //then
        then(productCommandService)
            .should()
            .modifyProduct(any(), any(), any());
    }

    @Test
    @DisplayName("controller : 상품 수정 테스트 : validation 상품 수정 실패")
    void failModifyProductByDtoValidationException() throws Exception {
        //given
        given(productService.isProductAuthorized(any(), any()))
            .willReturn(true);
        willThrow(new DtoValidationException("testFiled", "testMessage"))
            .given(productCommandService)
            .modifyProduct(any(), any(), any());

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, productEditUrl(1L))
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(makeRequestPartFixture(DEFAULT_REQUEST_MODIFY_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest());

        //then
        then(productCommandService)
            .should()
            .modifyProduct(any(), any(), any());
    }

    @Test
    @DisplayName("controller : 상품 수정 테스트 : 이미지 업로드 실패")
    void failModifyProductByImageUploadException() throws Exception {
        //given
        given(productService.isProductAuthorized(any(), any()))
            .willReturn(true);
        willThrow(ImageUploadException.class)
            .given(productCommandService)
            .modifyProduct(any(), any(), any());

        //when
        mockMvc.perform(multipart(HttpMethod.PUT, productEditUrl(1L))
                .file(REGISTER_PRODUCT_MULTIPART_1ST)
                .file(REGISTER_PRODUCT_MULTIPART_2ND)
                .file(REGISTER_PRODUCT_MULTIPART_3RD)
                .file(makeRequestPartFixture(DEFAULT_REQUEST_MODIFY_PRODUCT_DTO))
                .contentType("multipart/mixed")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        //then
        then(productCommandService)
            .should()
            .modifyProduct(any(), any(), any());
    }

}