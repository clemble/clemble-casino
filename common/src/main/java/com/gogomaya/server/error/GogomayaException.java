package com.gogomaya.server.error;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.gogomaya.server.error.GogomayaError.Code;

public class GogomayaException extends RuntimeException {

    /**
     * Generated value
     */
    private static final long serialVersionUID = -8129180501783483734L;

    final private GogomayaFailure failure;

    private GogomayaException(GogomayaFailure gogomayaFailure) {
        this.failure = gogomayaFailure;
    }

    public GogomayaFailure getFailure() {
        return failure;
    }

    public static GogomayaException create(String errorCode) {
        return create(Collections.singleton(errorCode));
    }

    public static GogomayaException create(GogomayaError gogomayaError) {
        return new GogomayaException(new GogomayaFailure(Collections.singleton(gogomayaError)));
    }

    public static GogomayaException create(GogomayaFailure gogomayaFailure) {
        return new GogomayaException(gogomayaFailure);
    }

    public static GogomayaException create(Collection<String> errorCodes) {
        errorCodes = errorCodes == null || errorCodes.size() == 0 ? Collections.singleton(Code.SERVER_ERROR_CODE) : errorCodes;

        Set<GogomayaError> verifiedErrors = new HashSet<GogomayaError>(errorCodes.size());
        for (String errorCode : errorCodes) {
            GogomayaError gogomayaError = GogomayaError.forCode(errorCode);
            verifiedErrors.add(gogomayaError != null ? gogomayaError : GogomayaError.ServerError);
        }

        GogomayaFailure gogomayaFailure = new GogomayaFailure(verifiedErrors);

        return new GogomayaException(gogomayaFailure);
    }
}
