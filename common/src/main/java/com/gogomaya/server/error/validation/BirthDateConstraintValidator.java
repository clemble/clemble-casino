package com.gogomaya.server.error.validation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BirthDateConstraintValidator implements ConstraintValidator<BirthDateConstraint, Date>{

    @Override
    public void initialize(BirthDateConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        return value == null || value.getTime() < System.currentTimeMillis() ? true : false;
    }

}
