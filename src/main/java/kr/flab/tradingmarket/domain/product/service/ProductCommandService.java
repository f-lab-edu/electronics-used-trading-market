package kr.flab.tradingmarket.domain.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.product.dto.request.RegisterProductDto;
import kr.flab.tradingmarket.domain.product.dto.request.RequestModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseModifyProductDto;
import kr.flab.tradingmarket.domain.product.dto.response.ResponseProductDetailDto;

public interface ProductCommandService {
    void registerProduct(RegisterProductDto registerProductDto, List<MultipartFile> images, Long userNo);

    ResponseModifyProductDto findByModifyProduct(Long productNo);

    ResponseProductDetailDto findByDetailProduct(Long productNo);

    void modifyProduct(Long productNo, RequestModifyProductDto modifyProduct, List<MultipartFile> images);

    void deleteProduct(Long productNo);
}
