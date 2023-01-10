package kr.flab.tradingmarket.domain.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.product.dto.RegisterProductDto;

public interface ProductCommandService {
    void registerProduct(RegisterProductDto registerProductDto, List<MultipartFile> images, Long userNo);
}
