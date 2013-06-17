package com.gogomaya.server.error;

import java.util.Collection;

public class GogomayaException extends RuntimeException {

    /**
     * Generated value
     */
    private static final long serialVersionUID = -8129180501783483734L;

    final private GogomayaFailureDescription failure;

    private GogomayaException(GogomayaFailureDescription failure) {
        this.failure = failure;
    }

    public GogomayaFailureDescription getFailureDescription() {
        return failure;
    }

    public static GogomayaException create(GogomayaError gogomayaError) {
        return new GogomayaException(GogomayaFailureDescription.create(gogomayaError));
    }

    public static GogomayaException create(GogomayaFailure gogomayaFailure) {
        return new GogomayaException(GogomayaFailureDescription.create(gogomayaFailure));
    }

    public static GogomayaException create(GogomayaFailureDescription failureDescription) {
        return new GogomayaException(failureDescription);
    }

    public static GogomayaException create(Collection<String> errorCodes) {
        return new GogomayaException(GogomayaFailureDescription.createForCodes(errorCodes));
    }

}
