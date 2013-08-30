package com.gogomaya.server.user;

import org.junit.Assert;
import org.junit.Test;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaError.Code;

public class GogomayaErrorTest {

    @Test
    public void testCodeValid() {
        Assert.assertTrue(GogomayaError.isValid(Code.BIRTH_DATE_INVALID_CODE));
    }

}