package kr.flab.tradingmarket.domain.category.entity;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"categoryNo"})
public class Category {
    private Long categoryNo;
    private String firstCategory;
    private String secondCategory;
    private String thirdCategory;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
