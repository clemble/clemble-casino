package com.clemble.casino.error;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

public class ClembleCasinoValidationService {

    final private ValidatorFactory validatorFactory;

    public ClembleCasinoValidationService(ValidatorFactory validatorFactory) {
        if (validatorFactory == null)
            throw new NullPointerException();
        this.validatorFactory = validatorFactory;
    }

    public <T> void validate(T object) {
        // Step 1. Validating provided input
        Set<ConstraintViolation<T>> errors = validatorFactory.getValidator().validate(object);
        if (errors.isEmpty())
            return;
        // Step 2. Accumulating error codes
        Set<String> errorCodes = new HashSet<String>();
        for (ConstraintViolation<T> error : errors) {
            errorCodes.add(error.getMessage());
        }
        // Step 3. Generating Gogomaya error
        if (errorCodes.size() > 0)
            throw ClembleCasinoException.fromCodes(errorCodes);
    }
}
