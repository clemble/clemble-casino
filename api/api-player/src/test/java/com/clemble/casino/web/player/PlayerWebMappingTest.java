package com.clemble.casino.web.player;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PlayerWebMappingTest {

    @Test
    public void testNotificationFormat() {
        assertEquals(String.format(PlayerWebMapping.PLAYER_NOTIFICATION_DOMAIN_PATTERN, "aaa", "example", "com"), "aaa_notif.example.com");
        assertEquals(String.format(PlayerWebMapping.PLAYER_NOTIFICATION_DOMAIN_PATTERN, "abc", "example", "com"), "abc_notif.example.com");
    }

}
