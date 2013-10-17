package com.clemble.casino.error;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClembleCasinoFailureDescription {

    final public static ClembleCasinoFailureDescription SERVER_ERROR = new ClembleCasinoFailureDescription().addProblem(new ClembleCasinoFailure(ClembleCasinoError.ServerError));

    final Set<ClembleCasinoFailure> failures = new HashSet<ClembleCasinoFailure>();

    public ClembleCasinoFailureDescription() {
    }

    public ClembleCasinoFailureDescription(Set<String> errorCodes) {
        this(ClembleCasinoError.forCodes(errorCodes));
    }

    @JsonCreator
    public ClembleCasinoFailureDescription(@JsonProperty("problems") Collection<ClembleCasinoError> errors) {
        for (ClembleCasinoError error : errors)
            this.failures.add(new ClembleCasinoFailure(error));
    }

    public Set<ClembleCasinoFailure> getProblems() {
        return failures;
    }

    public ClembleCasinoFailureDescription setProblems(Collection<ClembleCasinoFailure> failures) {
        this.failures.addAll(failures);
        return this;
    }

    public ClembleCasinoFailureDescription addProblem(ClembleCasinoFailure failure) {
        if (failure != null)
            this.failures.add(failure);
        return this;
    }

    public ClembleCasinoFailureDescription addError(ClembleCasinoError error) {
        this.failures.add(new ClembleCasinoFailure(error));
        return this;
    }

    public ClembleCasinoFailureDescription setErrors(Collection<ClembleCasinoError> errors) {
        for (ClembleCasinoError error : errors)
            this.failures.add(new ClembleCasinoFailure(error));
        return this;
    }

    @Override
    public String toString() {
        return "GogomayaFailureDescription [failures=" + failures + "]";
    }

}
