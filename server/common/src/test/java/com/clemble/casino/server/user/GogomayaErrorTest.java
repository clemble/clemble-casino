package com.clemble.casino.server.user;

import org.junit.Assert;
import org.junit.Test;

import com.clemble.casino.error.GogomayaError;
import com.clemble.casino.error.GogomayaError.Code;

public class GogomayaErrorTest {

    @Test
    public void testCodeValid() {
        Assert.assertTrue(GogomayaError.isValid(Code.BIRTH_DATE_INVALID_CODE));
    }

}
