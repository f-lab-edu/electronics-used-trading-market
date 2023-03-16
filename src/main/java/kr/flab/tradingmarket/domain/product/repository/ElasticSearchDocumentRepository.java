package kr.flab.tradingmarket.domain.product.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import kr.flab.tradingmarket.domain.product.entity.ProductSearch;

public interface ElasticSearchDocumentRepository extends ElasticsearchRepository<ProductSearch, Long> {
}
