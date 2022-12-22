package kr.flab.tradingmarket.domain.category.dto.response;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SimpleCategoryDto {
    private Long categoryNo;
    private String categoryName;
}
