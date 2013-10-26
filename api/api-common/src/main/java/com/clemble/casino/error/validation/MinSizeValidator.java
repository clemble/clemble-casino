package com.clemble.casino.error.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinSizeValidator implements ConstraintValidator<MinSize, String> {

    private int min;

    @Override
    public void initialize(MinSize constraintAnnotation) {
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.length() > min;
    }

}