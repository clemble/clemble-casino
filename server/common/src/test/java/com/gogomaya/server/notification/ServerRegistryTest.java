package com.gogomaya.server.notification;

import org.junit.Assert;
import org.junit.Test;

import com.gogomaya.server.LongServerRegistry;

public class ServerRegistryTest {

    @Test
    public void testNull() {
        LongServerRegistry serverRegistry = new LongServerRegistry();
        Assert.assertNull(serverRegistry.find(100L));
    }

    @Test
    public void testRangeRetrieval() {
        LongServerRegistry serverRegistry = new LongServerRegistry();
        serverRegistry.register(100L, "server1");
        Assert.assertEquals(serverRegistry.find(99L), "server1");
        Assert.assertEquals(serverRegistry.find(101L), "server1");
    }

    @Test
    public void testRangeRetrieval2() {
        LongServerRegistry serverRegistry = new LongServerRegistry();
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
