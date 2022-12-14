package kr.flab.tradingmarket.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;

@Mapper
@Repository
public interface UserMapper {

    void insertUser(User user);

    User findById(String userId);

    User findByNo(Long userNo);

    int countById(String userId);

    void delete(Long userNo);

    void updateUser(User user);

    void insertProfile(UserProfileImage imagePath);

    void updateUserProfile(@Param("imageNo") Long imageNo, @Param("userNo") Long userNo);

    User findUserProfileImageByNo(Long userNo);

    void deleteProfileImage(Long imageNo);

    void updateUserPassword(@Param("userNo") Long userNo, @Param("userPassword") String userPassword);
}
