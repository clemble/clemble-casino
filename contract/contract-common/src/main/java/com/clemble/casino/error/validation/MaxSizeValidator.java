package com.clemble.casino.error.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxSizeValidator implements ConstraintValidator<MaxSize, String> {

    private int max;

    @Override
    public void initialize(MaxSize constraintAnnotation) {
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.length() < max;
    }

}