package kr.flab.tradingmarket.domain.like.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.domain.like.entity.Like;

@Mapper
@Repository
public interface LikeMapper {

    List<Like> findByUserNoAndProductNo(Long userNo, List<Long> productNoList);
}
