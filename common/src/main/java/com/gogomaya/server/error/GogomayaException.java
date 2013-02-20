package com.gogomaya.server.error;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.gogomaya.server.error.GogomayaError.Code;
import com.google.common.collect.ImmutableSet;

public class GogomayaException extends RuntimeException {

    /**
     * Generated value
     */
    private static final long serialVersionUID = -8129180501783483734L;

    final private Set<GogomayaError> errorCodes;

    private GogomayaException(Set<GogomayaError> errorCodes) {
        this.errorCodes = ImmutableSet.<GogomayaError> copyOf(errorCodes);
    }

    public Set<GogomayaError> getErrorCodes() {
        return errorCodes;
    }

    public static GogomayaException create(String errorCode) {
        return create(Collections.singleton(errorCode));
    }

    public static GogomayaException create(Collection<String> errorCodes) {
        errorCodes = errorCodes == null || errorCodes.size() == 0 ? Collections.singleton(Code.SERVER_ERROR_CODE) : errorCodes;

        Set<GogomayaError> verifiedErrors = new HashSet<GogomayaError>(errorCodes.size());
        for (String errorCode : errorCodes) {
            GogomayaError gogomayaError = GogomayaError.forCode(errorCode);
            verifiedErrors.add(gogomayaError != null ? gogomayaError : GogomayaError.ServerError);
        }

        return new GogomayaException(verifiedErrors);
    }

}
