package kr.flab.tradingmarket.domain.product.dto.request;

import static org.springframework.data.domain.Sort.*;
import static org.springframework.data.domain.Sort.Direction.*;

public enum SearchOrder {
    DATE("modify_date desc", "modify_date", DESC),
    POPULAR("like_count desc", "like_count", DESC),
    PRICE_ASC("product_price asc", "product_price", ASC),
    PRICE_DESC("product_price desc", "product_price", DESC);

    private final String query;
    private final String fieldName;
    private final Direction direction;

    SearchOrder(String query, String fieldName, Direction direction) {
        this.query = query;
        this.fieldName = fieldName;
        this.direction = direction;
    }

    public String getQuery() {
        return query;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Direction getDirection() {
        return direction;
    }
}
