package kr.flab.tradingmarket.domain.like.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
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
}
