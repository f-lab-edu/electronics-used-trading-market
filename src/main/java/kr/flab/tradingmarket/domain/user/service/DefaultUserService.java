package kr.flab.tradingmarket.domain.user.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.response.MyInfoDto;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import kr.flab.tradingmarket.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    /**
     * 회원가입
     *
     * @param userDto
     * @throws UserIdDuplicateException 아이디 중복시에 리턴
     */
    @Override
    public void joinUser(JoinUserDto userDto) {

        if (isDuplicateUserId(userDto.getUserId())) {
            throw new UserIdDuplicateException("Duplicated userId");
        }
        User user = User.from(userDto);
        user.setEncryptionPassword(passwordEncoder.encode(user.getUserPassword()));
        try {
            userMapper.insertUser(user);
        } catch (DuplicateKeyException e) {
            throw new UserIdDuplicateException("Duplicated userId", e);
        }

    }

    /**
     * 아이디 중복확인
     *
     * @param userId
     * @return 중복된 아이디가 있을시에 true 없을시에 false
     */
    @Override
    public boolean isDuplicateUserId(String userId) {
        User findUser = userMapper.findById(userId);
        return findUser != null;
    }

    @Override
    public User findByUserNo(Long userNo) {
        User findUser = userMapper.findByNo(userNo);
        if (findUser == null) {
            throw new UserNotFoundException("User Not Found");
        }
        return findUser;
    }

    /**
     * 유저 수정
     *
     * @param modifyUserDto name,phone
     * @param userNo
     */
    @Override
    public void modifyUser(ModifyUserDto modifyUserDto, Long userNo) {
        userMapper.updateUser(User.from(modifyUserDto));
        loginService.logout();
    }

    /**
     * 유저삭제
     *
     * @param userNo
     */
    @Override
    public void withdrawUser(Long userNo) {
        userMapper.delete(userNo);

    }

    @Override
    public MyInfoDto findModifyUserDtoByUserNo(Long userNo) {
        return MyInfoDto.from(findByUserNo(userNo));
    }

    /**
     * 유저 프로필 이미지 변경
     *
     * @param imagePath
     * @param userNo
     */
    @Override
    @Transactional
    public UserProfileImage modifyUserProfile(UserProfileImage imagePath, Long userNo) {

        UserProfileImage userProfileImage = userMapper.findUserProfileImageByNo(userNo).getUserProfileImage();

        updateProfileImage(imagePath, userNo);

        if (userProfileImage == null) {
            return null;
        }

        return deleteImageProfileAndGetImage(userProfileImage);
    }

    @Override
    public void changePassword(ChangePasswordDto changePassword, Long userNo) {

        User findUser = findByUserNo(userNo);

        if (!passwordEncoder.matches(changePassword.getCurrentPassword(), findUser.getUserPassword())) {
            throw new PasswordNotMatchException("Password Not Matches");
        }

        userMapper.updateUserPassword(userNo, passwordEncoder.encode(changePassword.getPassword()));

    }

    private UserProfileImage deleteImageProfileAndGetImage(UserProfileImage userProfileImage) {
        userMapper.deleteProfileImage(userProfileImage.getImageNo());
        return userProfileImage;
    }

    private void updateProfileImage(UserProfileImage imagePath, Long userNo) {
        userMapper.insertProfile(imagePath);
        userMapper.updateUserProfile(imagePath.getImageNo(), userNo);
    }

}
