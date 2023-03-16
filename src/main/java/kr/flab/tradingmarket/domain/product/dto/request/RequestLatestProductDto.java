package kr.flab.tradingmarket.domain.product.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestLatestProductDto {
    private String lastModifiedDate;
    private Long lastProductNo;
    @Max(30)
    @Min(1)
    @NotNull
    private Integer size;
}
