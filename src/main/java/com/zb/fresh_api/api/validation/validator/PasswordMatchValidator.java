package com.zb.fresh_api.api.validation.validator;

import com.zb.fresh_api.api.validation.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String passwordField;
    private String confirmPasswordField;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.passwordField = constraintAnnotation.password();
        this.confirmPasswordField = constraintAnnotation.confirmPassword();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        Object passwordValue = new BeanWrapperImpl(obj).getPropertyValue(passwordField);
        Object confirmPasswordValue = new BeanWrapperImpl(obj).getPropertyValue(confirmPasswordField);

        if (passwordValue != null) {
            return passwordValue.equals(confirmPasswordValue);
        } else {
            return confirmPasswordValue == null;
        }
    }
}
