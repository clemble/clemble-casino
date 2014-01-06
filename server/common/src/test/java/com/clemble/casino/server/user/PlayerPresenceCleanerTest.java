package com.clemble.casino.server.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.clemble.casino.player.Presence;
import com.clemble.casino.server.event.SystemPlayerPresenceChangedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.PlayerPresenceSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PlayerPresenceCleanerTest.JUnitTestConfiguration.class)
public class PlayerPresenceCleanerTest {

    @Configurable
    @Import(PlayerPresenceSpringConfiguration.class)
    public static class JUnitTestConfiguration {

        @Bean(destroyMethod = "close")
        public JedisPubSubCache pubSubCache(JedisPool jedisPool) {
            return new JedisPubSubCache(jedisPool);
        }

    }

    @Autowired
    public ServerPlayerPresenceService presenceService;

    @Autowired
    public JedisPool jedisPool;

    @Autowired
    public SystemNotificationServiceListener systemListener;

    @Test
    public void testExpireWorks() throws InterruptedException {
        // Step 1. Mark player as online, and check his presence
        presenceService.markOnline("A");
        assertTrue(presenceService.isAvailable("A"));
        assertEquals(presenceService.getPresence("A").getPresence(), Presence.online);
        final BlockingQueue<SystemPlayerPresenceChangedEvent> events = new ArrayBlockingQueue<>(10);
        systemListener.subscribe("A", new SystemEventListener<SystemPlayerPresenceChangedEvent>() {
            @Override
            public void onEvent(String channel, SystemPlayerPresenceChangedEvent event) {
                events.add(event);
            }
        });
        Thread.sleep(500);
        assertTrue(events.isEmpty());
        // Step 2. Mark for expire in 1 second
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.expire("A", 1);
        } finally {
            jedisPool.returnResource(jedis);
        }
        // Step 4. Check player is offline
        SystemPlayerPresenceChangedEvent presenceChangedEvent = events.poll(5, TimeUnit.SECONDS);
        assertNotNull(presenceChangedEvent);
        assertEquals(presenceChangedEvent.getPresence(), Presence.offline);
        assertFalse(presenceService.isAvailable("A"));
        assertEquals(presenceService.getPresence("A").getPresence(), Presence.offline);
    }

}
