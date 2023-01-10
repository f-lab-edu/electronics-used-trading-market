package kr.flab.tradingmarket.common.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import kr.flab.tradingmarket.common.annotation.ValidImage;

public class ImageFileValidator implements ConstraintValidator<ValidImage, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> value, ConstraintValidatorContext context) {

        return (value.size() != 1 || !value.get(0).isEmpty()) && value.size() < 11;
    }

}