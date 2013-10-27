package com.clemble.casino;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

public class DNSBasedServerRegistryTest {

    @Test
    public void testWithSubstitution() {
        DNSBasedServerRegistry dnsBasedServerRegistry = new DNSBasedServerRegistry(3, "%s_notif.localhost.com", "%s_notif.localhost.com", "%s_notif.localhost.com");
        assertEquals(dnsBasedServerRegistry.findById("adc" + UUID.randomUUID().toString()), "adc_notif.localhost.com");
    }

    @Test
    public void testWithoutSubstitution() {
        DNSBasedServerRegistry dnsBasedServerRegistry = new DNSBasedServerRegistry(3, "localhost.com", "localhost.com", "localhost.com");
        assertEquals(dnsBasedServerRegistry.findById("adc" + UUID.randomUUID().toString()), "localhost.com");
    }

}
