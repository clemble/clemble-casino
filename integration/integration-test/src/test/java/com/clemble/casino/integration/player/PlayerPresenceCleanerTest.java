package com.clemble.casino.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.player.event.PlayerPresenceChangedEvent;
import com.clemble.casino.player.Presence;

@Ignore
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestSpringConfiguration.class)
public class PlayerPresenceCleanerTest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired
    public JedisPool jedisPool;

    @Test
    public void testExpireWorks() throws InterruptedException {
        // Step 1. Mark player as online, and check his presence
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        EventAccumulator<PlayerPresenceChangedEvent> accumulator = new EventAccumulator<>();
        A.listenerOperations().subscribeToPresenceEvents(A.getPlayer(), accumulator);
        assertTrue(accumulator.isEmpty());
        // Step 2. Mark for expire in 1 second
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.expire(A.getPlayer(), 1);
        } finally {
            jedisPool.returnResource(jedis);
        }
        // Step 4. Check player is offline
        PlayerPresenceChangedEvent presenceChangedEvent = accumulator.poll(2, TimeUnit.SECONDS);
        // Player notification is not immediate, it's done by background process, 
        // calling get triggers key expire event
        if(presenceChangedEvent == null) {
            jedis = jedisPool.getResource();
            try {
                jedis.get(A.getPlayer());
            }finally {
                jedisPool.returnResource(jedis);
            }
            presenceChangedEvent = accumulator.poll(5, TimeUnit.SECONDS);
        }
        assertNotNull(presenceChangedEvent);
        assertEquals(presenceChangedEvent.getPresence(), Presence.offline);
        assertEquals(A.presenceOperations().myPresence().getSession(), GameSessionKey.DEFAULT_SESSION);
        assertEquals(A.presenceOperations().myPresence().getPresence(), Presence.offline);
    }

}
