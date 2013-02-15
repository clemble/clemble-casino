package com.gogomaya.server.error;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

public class GogomayaValidationService {

    final private ValidatorFactory validatorFactory;

    public GogomayaValidationService(ValidatorFactory validatorFactory) {
        this.validatorFactory = checkNotNull(validatorFactory);
    }

    public <T> void validate(T object) {
        // Step 1. Validating provided input
        Set<ConstraintViolation<T>> errors = validatorFactory.getValidator().validate(object);
        if (errors.isEmpty())
            return;
        // Step 2. Accumulating error codes
        ArrayList<String> errorCodes = new ArrayList<String>();
        for (ConstraintViolation<T> error : errors) {
            errorCodes.add(error.getMessage());
        }
        // Step 3. Generating Gogomaya error
        if (errorCodes.size() > 0)
            throw GogomayaException.create(errorCodes);
    }
}
