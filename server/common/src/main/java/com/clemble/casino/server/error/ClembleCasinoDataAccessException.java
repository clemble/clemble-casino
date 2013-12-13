package com.clemble.casino.server.error;

import org.springframework.dao.DataAccessException;

import com.clemble.casino.error.ClembleCasinoException;

public class ClembleCasinoDataAccessException extends DataAccessException implements ClembleCasinoServerException {

    /**
     * Generated 13/12/13
     */
    private static final long serialVersionUID = -6807686742129584430L;

    final private ClembleCasinoException casinoException;

    public ClembleCasinoDataAccessException(ClembleCasinoException casinoException) {
        super(casinoException.getMessage(), casinoException.getCause());
        this.casinoException = casinoException;
    }

    @Override
    public ClembleCasinoException getCasinoException() {
        return casinoException;
    }
}
