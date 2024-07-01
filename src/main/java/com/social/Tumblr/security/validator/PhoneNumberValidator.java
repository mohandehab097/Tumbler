package com.social.Tumblr.security.validator;

import com.social.Tumblr.security.utils.Constants;
import com.social.Tumblr.security.annotations.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {


    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return Constants.PHONE_PATTERN.matcher(phone).matches();
    }
}


