package kr.flab.tradingmarket.domain.like.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = {"likeNo"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
    private Long likeNo;
    private Long userNo;
    private Long productNo;
    private LocalDateTime createDate;

    @Builder
    public Like(Long likeNo, Long userNo, Long productNo, LocalDateTime createDate) {
        this.likeNo = likeNo;
        this.userNo = userNo;
        this.productNo = productNo;
        this.createDate = createDate;
    }
}
