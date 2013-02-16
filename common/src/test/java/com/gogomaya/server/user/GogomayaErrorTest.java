package com.gogomaya.server.user;

import org.junit.Assert;
import org.junit.Test;

import com.gogomaya.server.error.GogomayaError;

public class GogomayaErrorTest {

    @Test
    public void testCodeValid() {
        Assert.assertTrue(GogomayaError.isValid(GogomayaError.BIRTH_DATE_INVALID_CODE));
    }

}
