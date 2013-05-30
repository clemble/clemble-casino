package com.gogomaya.server.error;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class GogomayaFailure {

    final private Set<GogomayaError> errors;

    final public static GogomayaFailure ServerError = new GogomayaFailure(Collections.singleton(GogomayaError.ServerError));

    public GogomayaFailure(GogomayaError gogomayaError) {
        errors = ImmutableSet.<GogomayaError> of(gogomayaError);
    }

    public GogomayaFailure(Iterable<GogomayaError> gogomayaErrors) {
        errors = ImmutableSet.<GogomayaError> copyOf(gogomayaErrors);
    }

    public Set<GogomayaError> getErrors() {
        return errors;
    }

}
