package kr.flab.tradingmarket.domain.user.controller;

import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.user.entity.User;
import kr.flab.tradingmarket.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.SUCCESS;
import static org.springframework.http.HttpStatus.OK;

/**
 * packageName :  kr.flab.tradingmarket.domain.user.controller
 * fileName : UserController
 * author :  ddh96
 * date : 2022-11-02
 * description : 유저관련 기능수행 RestApi 컨트롤러
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-11-02                ddh96             최초 생성
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ResponseMessage> joinUser(@RequestBody @Valid User user) {
        userService.joinUser(user);
        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(user)
                        .build());
    }

}
