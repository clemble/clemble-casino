package com.clemble.casino.error.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NickNameConstraintValidator implements ConstraintValidator<NickNameConstraint, String> {

    @Override
    public void initialize(NickNameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }

}
