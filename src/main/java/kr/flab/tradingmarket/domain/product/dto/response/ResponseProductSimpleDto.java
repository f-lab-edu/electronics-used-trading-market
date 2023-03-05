package kr.flab.tradingmarket.domain.product.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseProductSimpleDto {
    private final List<ProductSimpleDto> productList;
    private final Integer size;
    private final ResponsePageData<?> pageData;

    @Builder
    public ResponseProductSimpleDto(List<ProductSimpleDto> productList, Integer size, ResponsePageData<?> pageData) {
        this.productList = productList;
        this.size = size;
        this.pageData = pageData;
    }
}
