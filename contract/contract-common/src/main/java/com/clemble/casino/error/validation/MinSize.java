package com.clemble.casino.error.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Was not able to find separate {@link ConstraintValidator} for MIN size and MAX size, so created them myself. 
 * Get rid of this, if you'll find appropriate {@link ConstraintValidator}.
 * 
 * @author mavarazy
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinSizeValidator.class)
public @interface MinSize {

    int min() default 0;

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
