package com.clemble.casino.error.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.clemble.casino.error.ClembleCasinoError.Code;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeConstraintValidator.class)
public @interface AgeConstraint {

    String message() default Code.NICK_INVALID_CODE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
