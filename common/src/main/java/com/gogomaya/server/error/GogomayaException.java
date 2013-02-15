package com.gogomaya.server.error;

import java.util.Collection;

public class GogomayaException extends RuntimeException {

    /**
     * Generated value
     */
    private static final long serialVersionUID = -8129180501783483734L;

    private GogomayaException() {
    }

    public static GogomayaException create(String errorCodes) {
        return new GogomayaException();
    }

    public static GogomayaException create(Collection<String> errorCodes) {
        return new GogomayaException();
    }
}
