package com.gogomaya.server.integration.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.error.GogomayaFailure;
import com.gogomaya.server.error.GogomayaFailureDescription;
import com.google.common.collect.ImmutableList;

public class GogomayaErrorMatcher {

    private GogomayaErrorMatcher() {
        throw new IllegalAccessError();
    }

    public static Matcher<GogomayaException> create(final GogomayaError... errors) {
        final Collection<GogomayaError> expectedErrors = ImmutableList.<GogomayaError>copyOf(Arrays.asList(errors));
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
