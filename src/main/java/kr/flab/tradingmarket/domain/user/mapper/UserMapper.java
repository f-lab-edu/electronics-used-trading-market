package kr.flab.tradingmarket.domain.user.mapper;

import kr.flab.tradingmarket.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * packageName :  kr.flab.tradingmarket.domain.user.repository
 * fileName : UserMapper
 * author :  ddh96
 * date : 2022-11-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-11-02                ddh96             최초 생성
 */
@Mapper
@Repository
public interface UserMapper {

    Long insertUser(User user);


    User findById(String userId);

    User findByNo(Long userNo);
}
