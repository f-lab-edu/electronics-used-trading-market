package kr.flab.tradingmarket.domain.user.controller;

import kr.flab.tradingmarket.common.annotation.AuthCheck;
import kr.flab.tradingmarket.common.annotation.CurrentSession;
import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.image.exception.imageUploadException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.image.utils.ImageUtils;
import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;
import kr.flab.tradingmarket.domain.user.service.LoginService;
import kr.flab.tradingmarket.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.SUCCESS;
import static kr.flab.tradingmarket.domain.image.utils.ImageUtils.separateImagePath;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService;
    private final ImageService imageService;

    @PostMapping("/join")
    public ResponseEntity<ResponseMessage> joinUser(@RequestBody @Valid JoinUserDto joinDto) {
        userService.joinUser(joinDto);
        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(null)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logout() {
        loginService.logout();
        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(null)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody @Valid UserAuthDto loginDto) {
        loginService.login(loginDto);
        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(null)
                        .build());
    }


    @AuthCheck
    @PatchMapping("/my/info")
    public ResponseEntity<ResponseMessage> modifyUser(@RequestBody @Valid ModifyUserDto modifyUserDto, @CurrentSession Long userNo) {
        userService.modifyUser(modifyUserDto, userNo);
        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(null)
                        .build());
    }

    @AuthCheck
    @GetMapping("/my/info")
    public ResponseEntity<ResponseMessage> myInfo(@CurrentSession Long userNo) {
        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(userService.findModifyUserDtoByUserNo(userNo))
                        .build());
    }

    @AuthCheck
    @DeleteMapping("/my/withdraw")
    public ResponseEntity<ResponseMessage> withdrawUser(@CurrentSession Long userNo) {
        userService.withdrawUser(userNo);
        loginService.logout();
        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(null)
                        .build());
    }


    @AuthCheck
    @PutMapping("/my/profile-image")
    public ResponseEntity<ResponseMessage> modifyProfileImage(@CurrentSession Long userNo, MultipartFile image) throws IOException {
        String imageName = ImageUtils.getImageName();
        String imagePath = imageService.uploadImage(image, imageName, ImageType.PROFILE);

        UserProfileImage ImageEntity = UserProfileImage.builder()
                .fileLink(imagePath)
                .fileSize(image.getSize())
                .originalFileName(image.getOriginalFilename())
                .build();

        String deleteImageName = null;

        try {
            deleteImageName = userService.modifyUserProfile(ImageEntity, userNo);
        } catch (Exception e) {  // db 트랜잭션 실패시 버킷 이미지 삭제
            imageService.deleteImage(separateImagePath(imagePath));
            throw new imageUploadException("Image upload failed", e);
        }

        if (deleteImageName != null) {
            imageService.deleteImage(deleteImageName);
        }

        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(null)
                        .build());
    }

    @AuthCheck
    @PatchMapping("/my/password")
    public ResponseEntity<ResponseMessage> changePassword(@CurrentSession Long userNo, @RequestBody @Valid ChangePasswordDto changePasswordDto) {

        userService.changePassword(changePasswordDto, userNo);

        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(null)
                        .build());
    }
}
