package kr.flab.tradingmarket.domain.user.controller;

import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.user.dto.request.JoinUserDto;
import kr.flab.tradingmarket.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.SUCCESS;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ResponseMessage> joinUser(@RequestBody @Valid JoinUserDto joinDto) {
        userService.joinUser(joinDto);
        return ResponseEntity.status(OK)
                .body(new ResponseMessage.Builder(SUCCESS, OK.value())
                        .Result(null)
                        .build());
    }

}
