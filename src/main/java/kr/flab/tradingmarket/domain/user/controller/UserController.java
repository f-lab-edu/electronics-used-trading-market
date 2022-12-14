package kr.flab.tradingmarket.domain.user.controller;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static kr.flab.tradingmarket.domain.image.utils.ImageUtils.*;
import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.common.annotation.AuthCheck;
import kr.flab.tradingmarket.common.annotation.CurrentSession;
import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.image.service.ImageService;
import kr.flab.tradingmarket.domain.image.utils.ImageType;
import kr.flab.tradingmarket.domain.user.dto.request.ChangePasswordDto;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.ModifyUserDto;
import kr.flab.tradingmarket.domain.user.dto.request.UserAuthDto;
import kr.flab.tradingmarket.domain.user.entity.UserProfileImage;
import kr.flab.tradingmarket.domain.user.service.LoginService;
import kr.flab.tradingmarket.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

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
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logout() {
        loginService.logout();
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody @Valid UserAuthDto loginDto) {
        loginService.login(loginDto);
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }

    @AuthCheck
    @PatchMapping("/my/info")
    public ResponseEntity<ResponseMessage> modifyUser(@RequestBody @Valid ModifyUserDto modifyUserDto,
        @CurrentSession Long userNo) {
        userService.modifyUser(modifyUserDto, userNo);
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }

    @AuthCheck
    @GetMapping("/my/info")
    public ResponseEntity<ResponseMessage> myInfo(@CurrentSession Long userNo) {
        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .result(userService.findModifyUserDtoByUserNo(userNo))
                .build());
    }

    @AuthCheck
    @DeleteMapping("/my/withdraw")
    public ResponseEntity<ResponseMessage> withdrawUser(@CurrentSession Long userNo) {
        userService.withdrawUser(userNo);

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }

    @AuthCheck
    @PutMapping("/my/profile-image")
    public ResponseEntity<ResponseMessage> modifyProfileImage(@CurrentSession Long userNo, MultipartFile image) {

        String imagePath = imageService.uploadImage(image, ImageType.PROFILE);
        UserProfileImage ImageEntity = UserProfileImage.builder()
            .fileLink(imagePath)
            .fileSize(image.getSize())
            .originalFileName(image.getOriginalFilename())
            .build();

        String deleteImageName = null;

        try {
            deleteImageName = userService.modifyUserProfile(ImageEntity, userNo);
        } catch (Exception e) {  // db ???????????? ????????? ?????? ????????? ??????
            imageService.deleteImage(separateImagePath(imagePath));
            throw new ImageUploadException("Image upload failed", e);
        }

        if (deleteImageName != null) {
            imageService.deleteImage(deleteImageName);
        }

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }

    @AuthCheck
    @PatchMapping("/my/password")
    public ResponseEntity<ResponseMessage> changePassword(@CurrentSession Long userNo,
        @RequestBody @Valid ChangePasswordDto changePasswordDto) {

        userService.changePassword(changePasswordDto, userNo);

        return ResponseEntity.status(OK)
            .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                .build());
    }
}
