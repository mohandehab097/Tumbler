package com.social.Tumblr.security.validator;

import com.social.Tumblr.security.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.social.Tumblr.security.utils.Constants;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {


    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return false;
        }

        if (password.length() < Constants.MIN_LENGTH || password.length() > Constants.MAX_LENGTH) {
            return false;
        }

        if (!Constants.UPPERCASE_PATTERN.matcher(password).find()) {
            return false;
        }

        if (!Constants.LOWERCASE_PATTERN.matcher(password).find()) {
            return false;
        }

        if (!Constants.DIGIT_PATTERN.matcher(password).find()) {
            return false;
        }

        if (!Constants.SPECIAL_CHARACTER_PATTERN.matcher(password).find()) {
            return false;
        }

        return true;
    }

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

}

