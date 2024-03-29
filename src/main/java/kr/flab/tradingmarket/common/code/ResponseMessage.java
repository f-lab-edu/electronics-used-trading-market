package kr.flab.tradingmarket.common.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.springframework.validation.FieldError;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ResponseMessage {
    private final Status status;
    private final int code;
    private final String message;
    private final Object result;
    private final Map<String, String> validationMessage;

    private ResponseMessage(Builder builder) {
        status = builder.status;
        code = builder.code;
        message = builder.message;
        result = builder.result;
        validationMessage = builder.validationMessage;
    }

    public enum Status {
        SUCCESS, FAIL
    }

    /**
     * Builder 패턴적용 Status,code는 필수값 나머지는 선택
     */
    public static class Builder {
        private final Status status;
        private final int code;
        private String message = null;
        private Object result = null;
        private Map<String, String> validationMessage = null;

        public Builder(Status status, int code) {
            this.status = status;
            this.code = code;
        }

        public Builder result(Object result) {
            this.result = result;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseMessage build() {
            return new ResponseMessage(this);
        }

        public Builder validation(List<FieldError> validation) {
            if (validation != null) {
                addValidationMessage(validation);
            }
            return this;
        }

        public Builder validation(Set<ConstraintViolation<?>> error) {
            if (!error.isEmpty()) {
                addValidationMessage(error);
            }
            return this;
        }

        private void addValidationMessage(Set<ConstraintViolation<?>> error) {
            validationMessageEmptyCheck();
            for (ConstraintViolation<?> constraintViolation : error) {
                String fieldName = null;
                for (Path.Node node : constraintViolation.getPropertyPath()) {
                    fieldName = node.getName();
                }
                addValidationMessage(fieldName, constraintViolation.getMessage());
            }
        }

        public Builder validation(String fieldName, String message) {
            validationMessageEmptyCheck();
            addValidationMessage(fieldName, message);
            return this;
        }

        private void addValidationMessage(String fieldName, String message) {
            this.validationMessage.put(fieldName, message);
        }

        /**
         * json 포맷에 맞게 validationMessage 를 표현하기위해서 Map으로 가공해주는 메서드
         *
         * @return
         */
        private void addValidationMessage(List<FieldError> validation) {
            validationMessageEmptyCheck();
            for (FieldError allError : validation) {
                addValidationMessage(allError.getField(), allError.getDefaultMessage());
            }
        }

        private void validationMessageEmptyCheck() {
            if (this.validationMessage == null) {
                this.validationMessage = new HashMap<>();
            }
        }
    }

}
