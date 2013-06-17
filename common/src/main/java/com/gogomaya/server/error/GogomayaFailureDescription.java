package com.gogomaya.server.error;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GogomayaFailureDescription {

    final public static GogomayaFailureDescription SERVER_ERROR = new GogomayaFailureDescription().addProblem(new GogomayaFailure(GogomayaError.ServerError));

    final Set<GogomayaFailure> failures = new HashSet<GogomayaFailure>();

    public GogomayaFailureDescription() {
    }

    public GogomayaFailureDescription(Set<String> errorCodes) {
        this(GogomayaError.forCodes(errorCodes));
    }

    @JsonCreator
    public GogomayaFailureDescription(@JsonProperty("problems") Collection<GogomayaError> errors) {
        for (GogomayaError error : errors)
            this.failures.add(new GogomayaFailure(error));
    }

    public Set<GogomayaFailure> getProblems() {
        return failures;
    }

    public GogomayaFailureDescription setProblems(Collection<GogomayaFailure> failures) {
        this.failures.addAll(failures);
        return this;
    }

    public GogomayaFailureDescription addProblem(GogomayaFailure failure) {
        if (failure != null)
            this.failures.add(failure);
        return this;
    }

    public GogomayaFailureDescription addError(GogomayaError error) {
        this.failures.add(new GogomayaFailure(error));
        return this;
    }

    public GogomayaFailureDescription setErrors(Collection<GogomayaError> errors) {
        for (GogomayaError error : errors)
            this.failures.add(new GogomayaFailure(error));
        return this;
    }

}
