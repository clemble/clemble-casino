package com.clemble.casino.server.registration.service;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mavarazy on 8/16/14.
 */
public class GravatarServiceTest {

    @Test
    public void testUrl() {
        String url = GravatarService.toRedirect("example@exampl.com");
        Assert.assertEquals(url, "http://www.gravatar.com/avatar/a1531bbe8d2c7214ff96d9f8206e6a36");
    }

}
