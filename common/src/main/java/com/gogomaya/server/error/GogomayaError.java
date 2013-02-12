package com.gogomaya.server.error;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

final public class GogomayaError {

    final private Set<String> errorCodes;

    public GogomayaError(Collection<String> errorCodes) {
        this.errorCodes = ImmutableSet.<String>copyOf(errorCodes);
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }
}
