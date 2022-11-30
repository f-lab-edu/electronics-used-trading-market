package kr.flab.tradingmarket.common.advice;

import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.domain.image.exception.ExtensionNotSupportedException;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserAccessDeniedException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.FAIL;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class ExceptionAdvices {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("methodArgumentNotValidException ex : {}", ex.getFieldError());
        return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                        .message("유효성 검증 실패")
                        .validation(ex.getBindingResult().getFieldErrors())
                        .build());
    }

    @ExceptionHandler(UserIdDuplicateException.class)
    protected ResponseEntity<ResponseMessage> userIdDuplicateException(UserIdDuplicateException ex) {
        log.info("UserIdDuplicateException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                        .message("아이디가 중복됩니다.")
                        .build());
    }

    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<ResponseMessage> dateTimeParseException(DateTimeParseException ex) {
        log.info("DateTimeParseException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                        .message("유효성 검증 실패 : 날짜 필드 오류")
                        .build());
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    protected ResponseEntity<ResponseMessage> passwordNotMatchException(PasswordNotMatchException ex) {
        log.info("PasswordNotMatchException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, UNAUTHORIZED.value())
                        .message("비밀번호가 일치하지 않습니다.")
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ResponseMessage> userNotFoundException(UserNotFoundException ex) {
        log.info("UserNotFoundException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, UNAUTHORIZED.value())
                        .message("해당 아이디가 존재하지 않습니다.")
                        .build());
    }

    @ExceptionHandler(UserAccessDeniedException.class)
    protected ResponseEntity<ResponseMessage> userAccessDeniedException(UserAccessDeniedException ex) {
        log.info("UserAccessDeniedException ex : ", ex);
        return ResponseEntity.status(UNAUTHORIZED).body(
                new ResponseMessage.Builder(FAIL, UNAUTHORIZED.value())
                        .message("접근 권한이 없습니다. 로그인해주세요")
                        .Result("/login")
                        .build());
    }

    @ExceptionHandler(ExtensionNotSupportedException.class)
    protected ResponseEntity<ResponseMessage> extensionNotSupportedException(ExtensionNotSupportedException ex) {
        log.info("ExtensionNotSupportedException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                        .message(ex.getExtension() + "는 지원하지 않는 이미지 확장자입니다.")
                        .Result("")
                        .build());
    }

    @ExceptionHandler(ImageUploadException.class)
    protected ResponseEntity<ResponseMessage> imageUploadException(ImageUploadException ex) {
        log.info("ExtensionNotSupportedException ex : ", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                new ResponseMessage.Builder(FAIL, INTERNAL_SERVER_ERROR.value())
                        .message("이미지 업로드에 실패했습니다. 재시도 해주세요.")
                        .Result("")
                        .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResponseMessage> exception(Exception ex) {
        log.error("Exception ex : ", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                new ResponseMessage.Builder(FAIL, INTERNAL_SERVER_ERROR.value())
                        .message("서버 오류입니다. 복구될때까지 기다려주세요")
                        .Result("")
                        .build());
    }


}