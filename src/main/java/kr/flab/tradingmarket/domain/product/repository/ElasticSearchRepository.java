package kr.flab.tradingmarket.domain.product.repository;

import java.util.List;
import java.util.function.Function;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.domain.product.dto.request.ProductSearchDto;
import kr.flab.tradingmarket.domain.product.entity.ProductSearch;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ElasticSearchRepository implements ProductSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations;

    private NativeSearchQuery createSearchQuery(ProductSearchDto productSearchDto) {
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
            .withQuery(makeBoolQuery(productSearchDto))
            .withPageable(PageRequest.of(0, productSearchDto.getSize()))
            .withSort(Sort.by(productSearchDto.getOrder().getDirection(),
                    productSearchDto.getOrder().getFieldName())
                .and(Sort.by(Sort.Direction.DESC, "product_no")));
        return makeSearchAfter(productSearchDto, searchQuery);
    }

    @Override
    public List<ProductSearch> searchProduct(ProductSearchDto productSearchDto) {
        NativeSearchQuery query = createSearchQuery(productSearchDto);
        SearchHits<ProductSearch> searchDocuments = elasticsearchOperations.search(query, ProductSearch.class);
        return searchDocuments.get().map(SearchHit::getContent).toList();
    }

    private NativeSearchQuery makeSearchAfter(ProductSearchDto productSearchDto,
        NativeSearchQueryBuilder nativeSearchQueryBuilder) {

        if (productSearchDto.isLatestDateOrder()) {
            nativeSearchQueryBuilder.withSearchAfter(
                List.of(productSearchDto.getLastModifiedDate(), productSearchDto.getLastProductNo()));
            return nativeSearchQueryBuilder.build();
        }

        if (productSearchDto.isPriceOrder()) {
            nativeSearchQueryBuilder.withSearchAfter(
                List.of(productSearchDto.getLastPrice().toString(), productSearchDto.getLastProductNo()));
            return nativeSearchQueryBuilder.build();
        }

        if (productSearchDto.isPopularOrder()) {
            nativeSearchQueryBuilder.withSearchAfter(
                List.of(productSearchDto.getLastLikes(), productSearchDto.getLastProductNo()));
            return nativeSearchQueryBuilder.build();
        }

        return nativeSearchQueryBuilder.build();
    }

    private BoolQueryBuilder makeBoolQuery(ProductSearchDto productSearchDto) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        addFilterIfNotNull(boolQueryBuilder, productSearchDto.getCategory(),
            v -> QueryBuilders.matchQuery("product_category_no", productSearchDto.getCategory()));
        addFilterIfNotNull(boolQueryBuilder, productSearchDto.getProductStatus(),
            v -> QueryBuilders.matchQuery("product_status", productSearchDto.getProductStatus()));
        addFilterIfNotNull(boolQueryBuilder, productSearchDto.getProductExchangeStatus(),
            v -> QueryBuilders.matchQuery("product_exchange_status", productSearchDto.getProductExchangeStatus()));
        addFilterIfNotNull(boolQueryBuilder, productSearchDto.getProductSalesStatus(),
            v -> QueryBuilders.matchQuery("product_sales_status", productSearchDto.getProductSalesStatus()));

        addFilterIfNotNull(boolQueryBuilder, productSearchDto.getProductAsExpirationDate(),
            v -> QueryBuilders.rangeQuery("product_as_expiration_date").gte(v));
        addFilterIfNotNull(boolQueryBuilder, productSearchDto.getPurchaseDate(),
            v -> QueryBuilders.rangeQuery("product_purchase_date").gte(v));
        addFilterIfNotNull(boolQueryBuilder, productSearchDto.getMinPrice(),
            v -> QueryBuilders.rangeQuery("product_price").gte(v));
        addFilterIfNotNull(boolQueryBuilder, productSearchDto.getMaxPrice(),
            v -> QueryBuilders.rangeQuery("product_price").lte(v));

        return boolQueryBuilder.must(
            QueryBuilders.multiMatchQuery(productSearchDto.getKeyword(), "product_name", "product_content"));
    }

    private <T> void addFilterIfNotNull(BoolQueryBuilder boolQueryBuilder, T value,
        Function<T, QueryBuilder> filterFunction) {
        if (value != null) {
            boolQueryBuilder.filter(filterFunction.apply(value));
        }
    }

}
