package com.gogomaya.server.error.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.gogomaya.server.error.GogomayaError;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface PasswordConstraint {

    String message() default GogomayaError.PASSWORD_TOO_WEAK_CODE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
