package kr.flab.tradingmarket.common.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.common.annotation.ValidImage;
import kr.flab.tradingmarket.common.code.ValidationType;

public class ImageFileValidator implements ConstraintValidator<ValidImage, List<MultipartFile>> {

    private ValidationType type;

    @Override
    public void initialize(ValidImage validImage) {
        type = validImage.type();
    }

    @Override
    public boolean isValid(List<MultipartFile> value, ConstraintValidatorContext context) {
        if (type == ValidationType.INSERT) {
            return (value.size() != 1 || !value.get(0).isEmpty()) && value.size() < 11;
        }
        context.buildConstraintViolationWithTemplate("이미지는 10개 이하만 업로드 할 수있습니다.").addConstraintViolation();
        return value.size() < 11;

    }

}