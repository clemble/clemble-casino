package com.gogomaya.server.integration.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.error.GogomayaFailure;
import com.gogomaya.error.GogomayaFailureDescription;
import com.google.common.collect.ImmutableList;

public class GogomayaExceptionMatcherFactory {

    private GogomayaExceptionMatcherFactory() {
        throw new IllegalAccessError();
    }

    public static Matcher<GogomayaException> fromPossibleErrors(final GogomayaError... errors) {
        final Collection<GogomayaError> expectedErrors = ImmutableList.<GogomayaError> copyOf(Arrays.asList(errors));
        return new CustomMatcher<GogomayaException>(Arrays.toString(errors)) {

            @Override
            public boolean matches(Object item) {
                // Step 1. Sanity check
                if (!(item instanceof GogomayaException))
                    return false;
                // Step 2. Checking value
                GogomayaFailureDescription failureDescription = ((GogomayaException) item).getFailureDescription();
                if (failureDescription == null || failureDescription.getProblems() == null || failureDescription.getProblems().isEmpty())
                    return false;
                // Step 3. Accumulating errors
                for (GogomayaFailure failure : failureDescription.getProblems()) {
                    if (expectedErrors.contains(failure.getError()))
                        return true;
                }
                // Step 4. No possible errors found
                return false;
            }
        };

    }

    public static Matcher<GogomayaException> fromErrors(final GogomayaError... errors) {
        final Collection<GogomayaError> expectedErrors = ImmutableList.<GogomayaError> copyOf(Arrays.asList(errors));
        return new CustomMatcher<GogomayaException>(Arrays.toString(errors)) {

            @Override
            public boolean matches(Object item) {
                // Step 1. Sanity check
                if (!(item instanceof GogomayaException))
                    return false;
                // Step 2. Checking value
                GogomayaFailureDescription failureDescription = ((GogomayaException) item).getFailureDescription();
                if (failureDescription == null || failureDescription.getProblems() == null || failureDescription.getProblems().isEmpty())
                    return false;
                // Step 3. Accumulating errors
                Collection<GogomayaError> actualErrors = new ArrayList<>();
                for (GogomayaFailure failure : failureDescription.getProblems()) {
                    actualErrors.add(failure.getError());
                }
                // Step 4. Checking errors
                return expectedErrors.containsAll(actualErrors) && actualErrors.containsAll(expectedErrors);
            }
        };
    }

}
