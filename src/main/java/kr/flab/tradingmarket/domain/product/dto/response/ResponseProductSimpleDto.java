package kr.flab.tradingmarket.domain.product.dto.response;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseProductSimpleDto {
    private final List<ProductSimpleDto> productList;
    private final Integer size;
    private final Map<String, Object> pageData;

    @Builder
    public ResponseProductSimpleDto(List<ProductSimpleDto> productList, Integer size, Map<String, Object> pageData) {
        this.productList = productList;
        this.size = size;
        this.pageData = pageData;
    }
}
