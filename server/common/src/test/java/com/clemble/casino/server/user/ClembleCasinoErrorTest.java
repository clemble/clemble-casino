package com.clemble.casino.server.user;

import org.junit.Assert;
import org.junit.Test;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoError.Code;

public class ClembleCasinoErrorTest {

    @Test
    public void testCodeValid() {
        Assert.assertTrue(ClembleCasinoError.isValid(Code.BIRTH_DATE_INVALID_CODE));
    }

}
