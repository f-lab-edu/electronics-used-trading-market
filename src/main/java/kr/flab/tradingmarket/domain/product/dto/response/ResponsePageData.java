package kr.flab.tradingmarket.domain.product.dto.response;

import kr.flab.tradingmarket.domain.product.dto.request.SearchOrder;
import lombok.Getter;

@Getter
public class ResponsePageData<T> {
    private final T lastValue;
    private final String lastValueName;
    private final SearchOrder order;
    private final Long lastProductNo;

    public ResponsePageData(T lastValue, SearchOrder order, Long lastProductNo) {
        this.lastValue = lastValue;
        this.lastValueName = order.getResponseFieldName();
        this.order = order;
        this.lastProductNo = lastProductNo;
    }
}
