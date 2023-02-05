package kr.flab.tradingmarket.domain.user.service;

import static kr.flab.tradingmarket.domain.image.utils.ImageUtils.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.response.MyInfoDto;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultUserCommandService implements UserCommandService {
    private final ImageService imageService;
    private final UserService userService;

    @Override
    public void joinUser(JoinUserDto userDto) {
        userService.joinUser(userDto);
    }

    @Override
    public void modifyUser(ModifyUserDto modifyUserDto, Long userId) {
        userService.modifyUser(modifyUserDto, userId);
    }

    @Override
    public void withdrawUser(Long userId) {
        userService.withdrawUser(userId);
    }

    @Override
    public MyInfoDto findModifyUserDtoByUserNo(Long userNo) {
        return userService.findModifyUserDtoByUserNo(userNo);
    }

    @Override
    public void modifyUserProfile(Long userNo, MultipartFile image) {

        String imagePath = imageService.uploadImage(image, ImageType.PROFILE);

        UserProfileImage imageEntity = UserProfileImage.builder()
            .fileLink(imagePath)
            .fileSize(image.getSize())
            .originalFileName(image.getOriginalFilename())
            .build();

        String deleteImageName = null;

        try {
            deleteImageName = userService.modifyUserProfile(imageEntity, userNo);
        } catch (Exception e) {
            imageService.deleteImage(separateImagePath(imagePath));
            throw new ImageUploadException("Image upload failed", e);
        }

        if (deleteImageName != null) {
            imageService.deleteImage(deleteImageName);
        }

    }

    @Override
    public void changePassword(ChangePasswordDto changePassword, Long userNo) {
        userService.changePassword(changePassword, userNo);
    }
}
