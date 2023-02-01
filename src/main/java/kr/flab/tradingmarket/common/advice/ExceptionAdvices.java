package kr.flab.tradingmarket.common.advice;

import static kr.flab.tradingmarket.common.code.ResponseMessage.Status.*;
import static org.springframework.http.HttpStatus.*;

import java.time.format.DateTimeParseException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.JsonMappingException;

import kr.flab.tradingmarket.common.code.ResponseMessage;
import kr.flab.tradingmarket.common.exception.DtoValidationException;
import kr.flab.tradingmarket.common.exception.NoPermissionException;
import kr.flab.tradingmarket.domain.image.exception.ExtensionNotSupportedException;
import kr.flab.tradingmarket.domain.image.exception.ImageUploadException;
import kr.flab.tradingmarket.domain.product.exception.ProductModifyException;
import kr.flab.tradingmarket.domain.product.exception.ProductRegisterException;
import kr.flab.tradingmarket.domain.user.exception.PasswordNotMatchException;
import kr.flab.tradingmarket.domain.user.exception.UserAccessDeniedException;
import kr.flab.tradingmarket.domain.user.exception.UserIdDuplicateException;
import kr.flab.tradingmarket.domain.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionAdvices {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("MethodArgumentNotValidException ex : {}", ex.getFieldError());
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message("유효성 검증 실패")
                .validation(ex.getBindingResult().getFieldErrors())
                .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ResponseMessage> constraintViolationException(ConstraintViolationException ex) {
        log.info("ConstraintViolationException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message("유효성 검증 실패")
                .validation(ex.getConstraintViolations())
                .build());
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ResponseMessage> constraintViolationException(BindException ex) {
        log.info("BindException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message("유효성 검증 실패")
                .validation(ex.getFieldErrors())
                .build());
    }

    @ExceptionHandler(DtoValidationException.class)
    protected ResponseEntity<ResponseMessage> imageCountExceededException(DtoValidationException ex) {
        log.info("DtoValidationException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message("유효성 검증 실패")
                .validation(ex.getField(), ex.getMessage())
                .build());
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ResponseMessage> imageCountExceededException(ValidationException ex) {
        log.info("ValidationException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message("유효성 검증 실패")
                .validation("field", "형식에 맞지않는 필드가 존재합니다.")
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ResponseMessage> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof JsonMappingException jsonMappingException) {
            log.info("JsonMappingException ex : ", ex);
            return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                    .message("유효성 검증 실패")
                    .validation(jsonMappingException.getPath().get(0).getFieldName(), "형식에 맞지않습니다.")
                    .build());
        }
        if (rootCause instanceof DateTimeParseException dateTimeParseException) {
            log.info("dateTimeParseException ex : ", ex);
            return ResponseEntity.status(BAD_REQUEST).body(
                new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                    .message("유효성 검증 실패")
                    .validation(dateTimeParseException.getParsedString(), " : 날짜 형식이 맞지않습니다.")
                    .build());
        }
        log.info("HttpMessageNotReadableException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message("유효성 검증 실패")
                .validation("field", "형식에 맞지않는 필드가 존재합니다.")
                .build());
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    protected ResponseEntity<ResponseMessage> passwordNotMatchException(PasswordNotMatchException ex) {
        log.info("PasswordNotMatchException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message("비밀번호가 일치하지 않습니다.")
                .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ResponseMessage> userNotFoundException(UserNotFoundException ex) {
        log.info("UserNotFoundException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message("해당 아이디가 존재하지 않습니다.")
                .build());
    }

    @ExceptionHandler(UserAccessDeniedException.class)
    protected ResponseEntity<ResponseMessage> userAccessDeniedException(UserAccessDeniedException ex) {
        log.info("UserAccessDeniedException ex : ", ex);
        return ResponseEntity.status(UNAUTHORIZED).body(
            new ResponseMessage.Builder(FAIL, UNAUTHORIZED.value())
                .message("접근 권한이 없습니다. 로그인해주세요")
                .result("/login")
                .build());
    }

    @ExceptionHandler(ExtensionNotSupportedException.class)
    protected ResponseEntity<ResponseMessage> extensionNotSupportedException(ExtensionNotSupportedException ex) {
        log.info("ExtensionNotSupportedException ex : ", ex);
        return ResponseEntity.status(BAD_REQUEST).body(
            new ResponseMessage.Builder(FAIL, BAD_REQUEST.value())
                .message(ex.getExtension() + "는 지원하지 않는 이미지 확장자입니다.")
                .build());
    }

    @ExceptionHandler(ImageUploadException.class)
    protected ResponseEntity<ResponseMessage> imageUploadException(ImageUploadException ex) {
        log.info("ImageUploadException ex : ", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
            new ResponseMessage.Builder(FAIL, INTERNAL_SERVER_ERROR.value())
                .message("이미지 업로드에 실패했습니다. 나중에 다시시도 해주세요.")
                .build());
    }

    @ExceptionHandler(ProductRegisterException.class)
    protected ResponseEntity<ResponseMessage> productRegisterException(ProductRegisterException ex) {
        log.info("ProductRegisterException ex : ", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
            new ResponseMessage.Builder(FAIL, INTERNAL_SERVER_ERROR.value())
                .message("상품등록에 실패했습니다. 나중에 다시시도 해주세요.")
                .build());
    }

    @ExceptionHandler(ProductModifyException.class)
    protected ResponseEntity<ResponseMessage> productModifyException(ProductModifyException ex) {
        log.info("ProductModifyException ex : ", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
            new ResponseMessage.Builder(FAIL, INTERNAL_SERVER_ERROR.value())
                .message("상품수정에 실패했습니다. 나중에 다시시도 해주세요.")
                .build());
    }

    @ExceptionHandler(NoPermissionException.class)
    protected ResponseEntity<ResponseMessage> noPermissionException(NoPermissionException ex) {
        log.info("NoPermissionException ex : ", ex);
        return ResponseEntity.status(UNAUTHORIZED).body(
            new ResponseMessage.Builder(FAIL, UNAUTHORIZED.value())
                .message("권한이 없습니다.")
                .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResponseMessage> exception(Exception ex) {
        log.error("Exception ex : ", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
            new ResponseMessage.Builder(FAIL, INTERNAL_SERVER_ERROR.value())
                .message("서버 오류입니다. 복구될때까지 기다려주세요")
                .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ResponseMessage> noHandlerFoundException(NoHandlerFoundException ex) {
        log.info("NoHandlerFoundException ex : ", ex);
        return ResponseEntity.status(NOT_FOUND).body(
            new ResponseMessage.Builder(FAIL, NOT_FOUND.value())
                .message("잘못된 URL 입니다.")
                .build());
    }

}