package com.clemble.casino.error;

import java.util.Collection;

public class GogomayaException extends RuntimeException {

    /**
     * Generated value
     */
    private static final long serialVersionUID = -8129180501783483734L;

    final private GogomayaFailureDescription failure;

    private GogomayaException(GogomayaFailureDescription failure) {
        super(failure.toString());
        this.failure = failure;
    }

    public GogomayaFailureDescription getFailureDescription() {
        return failure;
    }

    public static GogomayaException fromError(GogomayaError error) {
        return new GogomayaException(new GogomayaFailureDescription().addError(error));
    }

    public static GogomayaException fromFailures(Collection<GogomayaFailure> failures) {
        return new GogomayaException(new GogomayaFailureDescription().setProblems(failures));
    }

    public static GogomayaException fromCodes(Collection<String> errors) {
        return new GogomayaException(new GogomayaFailureDescription().setErrors(GogomayaError.forCodes(errors)));
    }

    public static GogomayaException fromDescription(GogomayaFailureDescription description) {
        return new GogomayaException(description);
    }

}
