package com.gogomaya.server.error.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordConstraintValidator implements ConstraintValidator<PasswordConstraint, String>{

    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.equals("123456");
    }

}
