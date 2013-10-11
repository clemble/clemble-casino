package com.clemble.casino.error.validation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AgeConstraintValidator implements ConstraintValidator<AgeConstraint, Date>{

    @Override
    public void initialize(AgeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        return value == null || value.getTime() < System.currentTimeMillis() ? true : false;
    }

}
