package com.clemble.casino.error;

import java.util.Collection;

public class ClembleCasinoException extends RuntimeException {

    /**
     * Generated value
     */
    private static final long serialVersionUID = -8129180501783483734L;

    final private ClembleCasinoFailureDescription failure;

    private ClembleCasinoException(ClembleCasinoFailureDescription failure) {
        super(failure.toString());
        this.failure = failure;
    }

    public ClembleCasinoFailureDescription getFailureDescription() {
        return failure;
    }

    public static ClembleCasinoException fromError(ClembleCasinoError error) {
        return new ClembleCasinoException(new ClembleCasinoFailureDescription().addError(error));
    }

    public static ClembleCasinoException fromFailures(Collection<ClembleCasinoFailure> failures) {
        return new ClembleCasinoException(new ClembleCasinoFailureDescription().setProblems(failures));
    }

    public static ClembleCasinoException fromCodes(Collection<String> errors) {
        return new ClembleCasinoException(new ClembleCasinoFailureDescription().setErrors(ClembleCasinoError.forCodes(errors)));
    }

    public static ClembleCasinoException fromDescription(ClembleCasinoFailureDescription description) {
        return new ClembleCasinoException(description);
    }

}
