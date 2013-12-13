package com.clemble.casino.server.error;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import com.clemble.casino.error.ClembleCasinoException;

public class ClembleConstraintExceptionResolver implements PersistenceExceptionTranslator {

    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return check(ex);
    }

    private DataAccessException check(Throwable ex) {
        if (ex == null)
            return null;
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) ex;
            ClembleCasinoException casinoException = ClembleCasinoException.fromGenericConstraintViolations(violationException.getConstraintViolations());
            casinoException.setStackTrace(ex.getStackTrace());
            return new ClembleCasinoDataAccessException(casinoException);
        } else {
            return check(ex.getCause());
        }
    }

}
