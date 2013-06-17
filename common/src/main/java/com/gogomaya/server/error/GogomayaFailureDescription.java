package com.gogomaya.server.error;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.error.GogomayaError.Code;
import com.google.common.collect.ImmutableSet;

public class GogomayaFailureDescription {

    final public static GogomayaFailureDescription SERVER_ERROR = GogomayaFailureDescription.create(GogomayaError.ServerError);

    final Set<GogomayaFailure> failures;

    private GogomayaFailureDescription(Collection<GogomayaFailure> failures) {
        this.failures = ImmutableSet.copyOf(failures);
    }

    private GogomayaFailureDescription(GogomayaFailure gogomayaFailure) {
        this.failures = ImmutableSet.<GogomayaFailure> of(gogomayaFailure);
    }

    public Set<GogomayaFailure> getProblems() {
        return failures;
    }

    public static GogomayaFailureDescription create(GogomayaError gogomayaError) {
        return new GogomayaFailureDescription(new GogomayaFailure(gogomayaError));
    }

    @JsonCreator
    public static GogomayaFailureDescription create(@JsonProperty("problems") Collection<GogomayaError> gogomayaError) {
        Collection<GogomayaFailure> failures = new ArrayList<GogomayaFailure>();
        for (GogomayaError error : gogomayaError) {
            if (error != null)
                failures.add(new GogomayaFailure(error));
        }
        return new GogomayaFailureDescription(failures);
    }

    public static GogomayaFailureDescription create(GogomayaFailure gogomayaFailure) {
        return new GogomayaFailureDescription(gogomayaFailure);
    }

    public static GogomayaFailureDescription createForCodes(Collection<String> errorCodes) {
        errorCodes = errorCodes == null || errorCodes.size() == 0 ? Collections.singleton(Code.SERVER_ERROR_CODE) : errorCodes;

        Set<GogomayaError> verifiedErrors = new HashSet<GogomayaError>(errorCodes.size());
        for (String errorCode : errorCodes) {
            GogomayaError gogomayaError = GogomayaError.forCode(errorCode);
            verifiedErrors.add(gogomayaError != null ? gogomayaError : GogomayaError.ServerError);
        }

        return create(verifiedErrors);
    }

}
