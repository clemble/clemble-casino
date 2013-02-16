package com.gogomaya.server.error;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class GogomayaException extends RuntimeException {

    /**
     * Generated value
     */
    private static final long serialVersionUID = -8129180501783483734L;

    final private Set<String> errorCodes;

    private GogomayaException(Set<String> errorCodes) {
        this.errorCodes = ImmutableSet.<String>copyOf(errorCodes);
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public static GogomayaException create(String errorCode) {
        return create(Collections.singleton(errorCode));
    }

    public static GogomayaException create(Collection<String> errorCodes) {
        errorCodes = errorCodes == null || errorCodes.size() == 0 ? Collections.singleton(GogomayaError.SERVER_ERROR_CODE) : errorCodes;

        Set<String> verifiedErrors = new HashSet<String>(errorCodes.size());
        for (String errorCode : errorCodes) {
            if (GogomayaError.isValid(errorCode)) {
                verifiedErrors.add(errorCode);
            } else {
                verifiedErrors.add(GogomayaError.SERVER_ERROR_CODE);
            }
        }

        return new GogomayaException(verifiedErrors);
    }

}
