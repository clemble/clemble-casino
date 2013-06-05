package com.gogomaya.server.notification;

import org.junit.Assert;
import org.junit.Test;

public class ServerRegistryTest {

    @Test
    public void testNull() {
        ServerRegistry serverRegistry = new ServerRegistry();
        Assert.assertNull(serverRegistry.find(100L));
    }

    @Test
    public void testRangeRetrieval() {
        ServerRegistry serverRegistry = new ServerRegistry();
        serverRegistry.register(100L, "server1");
        Assert.assertEquals(serverRegistry.find(99L), "server1");
        Assert.assertEquals(serverRegistry.find(101L), "server1");
    }

    @Test
    public void testRangeRetrieval2() {
        ServerRegistry serverRegistry = new ServerRegistry();
        serverRegistry.register(100L, "server1");
        serverRegistry.register(1000L, "server2");
        Assert.assertEquals(serverRegistry.find(-9L), "server1");
        Assert.assertEquals(serverRegistry.find(99L), "server1");
        Assert.assertEquals(serverRegistry.find(100L), "server1");
        Assert.assertEquals(serverRegistry.find(101L), "server2");
        Assert.assertEquals(serverRegistry.find(1000L), "server2");
        Assert.assertEquals(serverRegistry.find(1001L), "server2");
    }
}
