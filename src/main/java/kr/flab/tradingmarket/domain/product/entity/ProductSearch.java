package kr.flab.tradingmarket.domain.product.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Builder;
import lombok.Getter;

@Document(indexName = "products_index_v3")
@Getter
@Builder
public class ProductSearch {
    @Id
    private Long productNo;
    @Field(type = FieldType.Text, name = "product_name")
    private String productName;
    @Field(type = FieldType.Double, name = "product_price")
    private Double productPrice;
    @Field(type = FieldType.Date, name = "modify_date")
    private LocalDateTime modifyDate;
    @Field(type = FieldType.Text, name = "image_link")
    private String image;
    @Field(type = FieldType.Integer, name = "like_count")
    private Integer likeCount;
    @Field(type = FieldType.Text, name = "product_sales_status")
    private ProductSalesStatus productSalesStatus;
}
