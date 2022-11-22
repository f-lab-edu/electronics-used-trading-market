package kr.flab.tradingmarket.domain.user.mapper;

import kr.flab.tradingmarket.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface UserMapper {

    Long insertUser(User user);

    User findById(String userId);

    User findByNo(Long userNo);

    int countById(String userId);

    void delete(Long userNo);

    void updateUser(User user);
}
