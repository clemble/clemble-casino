package com.clemble.casino.integration.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoFailure;
import com.clemble.casino.error.ClembleCasinoFailureDescription;
import com.clemble.casino.utils.CollectionUtils;

public class ClembleCasinoExceptionMatcherFactory {

    private ClembleCasinoExceptionMatcherFactory() {
        throw new IllegalAccessError();
    }

    public static Matcher<ClembleCasinoException> fromPossibleErrors(final ClembleCasinoError... errors) {
        final Collection<ClembleCasinoError> expectedErrors = CollectionUtils.<ClembleCasinoError> immutableList(Arrays.asList(errors));
        return new CustomMatcher<ClembleCasinoException>(Arrays.toString(errors)) {

            @Override
            public boolean matches(Object item) {
                // Step 1. Sanity check
                if (!(item instanceof ClembleCasinoException))
                    return false;
                // Step 2. Checking value
                ClembleCasinoFailureDescription failureDescription = ((ClembleCasinoException) item).getFailureDescription();
                if (failureDescription == null || failureDescription.getProblems() == null || failureDescription.getProblems().isEmpty())
                    return false;
                // Step 3. Accumulating errors
                for (ClembleCasinoFailure failure : failureDescription.getProblems()) {
                    if (expectedErrors.contains(failure.getError()))
                        return true;
                }
                // Step 4. No possible errors found
                return false;
            }
        };

    }

    public static Matcher<ClembleCasinoException> fromErrors(final ClembleCasinoError... errors) {
        final Collection<ClembleCasinoError> expectedErrors = CollectionUtils.<ClembleCasinoError> immutableList(Arrays.asList(errors));
        return new CustomMatcher<ClembleCasinoException>(Arrays.toString(errors)) {

            @Override
            public boolean matches(Object item) {
                // Step 1. Sanity check
                if (!(item instanceof ClembleCasinoException))
                    return false;
                // Step 2. Checking value
                ClembleCasinoFailureDescription failureDescription = ((ClembleCasinoException) item).getFailureDescription();
                if (failureDescription == null || failureDescription.getProblems() == null || failureDescription.getProblems().isEmpty())
                    return false;
                // Step 3. Accumulating errors
                Collection<ClembleCasinoError> actualErrors = new ArrayList<>();
                for (ClembleCasinoFailure failure : failureDescription.getProblems()) {
                    actualErrors.add(failure.getError());
                }
                // Step 4. Checking errors
                return expectedErrors.containsAll(actualErrors) && actualErrors.containsAll(expectedErrors);
            }
        };
    }

}
