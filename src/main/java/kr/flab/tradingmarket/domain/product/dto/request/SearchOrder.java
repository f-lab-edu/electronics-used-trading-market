package kr.flab.tradingmarket.domain.product.dto.request;

import static org.springframework.data.domain.Sort.*;
import static org.springframework.data.domain.Sort.Direction.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SearchOrder {
    DATE("modify_date desc", "modify_date", DESC, "lastModifiedDate"),
    POPULAR("like_count desc", "like_count", DESC, "lastLikes"),
    PRICE_ASC("product_price asc", "product_price", ASC, "lastPrice"),
    PRICE_DESC("product_price desc", "product_price", DESC, "lastPrice");

    private final String query;
    private final String fieldName;
    private final Direction direction;
    private final String responseFieldName;

}
