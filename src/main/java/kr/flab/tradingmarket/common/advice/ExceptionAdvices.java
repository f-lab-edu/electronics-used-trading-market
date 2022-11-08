package kr.flab.tradingmarket.common.advice;

import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.FAIL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class ExceptionAdvices {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseMessage> validation(MethodArgumentNotValidException ex) {
        log.info("validation ex : {}", ex.getFieldError());
        return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                        .message("유효성 검증 실패")
                        .validation(ex.getBindingResult().getFieldErrors())
                        .build());
    }

    @ExceptionHandler(UserIdDuplicateException.class)
    protected ResponseEntity<ResponseMessage> validation(UserIdDuplicateException ex) {
        log.info("userIdDuplicate ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                        .message("아이디가 중복됩니다.")
                        .build());
    }
}
