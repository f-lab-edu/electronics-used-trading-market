package kr.flab.tradingmarket.domain.category.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = {"categoryNo"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    private Long categoryNo;
    private String firstCategory;
    private String secondCategory;
    private String thirdCategory;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @Builder
    public Category(Long categoryNo, String firstCategory, String secondCategory, String thirdCategory,
        LocalDateTime createDate, LocalDateTime modifyDate) {
        this.categoryNo = categoryNo;
        this.firstCategory = firstCategory;
        this.secondCategory = secondCategory;
        this.thirdCategory = thirdCategory;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }
}
