package com.gogomaya.server.player.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.player.PlayerState;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PlayerCommonSpringConfiguration.class })
public class PlayerStateManagerTest {

    final private Random RANDOM = new Random();

    @Autowired
    public PlayerStateManager playerStateManager;

    @Test
    public void testMarkAvailable() {
        long player = new Random().nextLong();

        Assert.assertFalse(playerStateManager.isAvailable(player));

        playerStateManager.markAvailable(player);

        Assert.assertTrue(playerStateManager.isAvailable(player));
    }

    @Test(expected = GogomayaException.class)
    public void testMarkActiveIncorrect() {
        long player = RANDOM.nextLong();
        long session = RANDOM.nextLong();

        Assert.assertFalse(playerStateManager.isAvailable(player));

        playerStateManager.markBusy(player, session);
    }

    @Test
    public void testMarkActive() {
        long player = RANDOM.nextLong();
        long session = RANDOM.nextLong();

        Assert.assertFalse(playerStateManager.isAvailable(player));

        playerStateManager.markAvailable(player);

        Assert.assertTrue(playerStateManager.isAvailable(player));

        playerStateManager.markBusy(player, session);

        Assert.assertFalse(playerStateManager.isAvailable(player));
        Assert.assertEquals(playerStateManager.getActiveSession(player), Long.valueOf(session));
    }

    @Test
    public void testMarkCollectionActive() {
        Collection<Long> players = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            players.add(RANDOM.nextLong());
        long session = RANDOM.nextLong();

        for (Long player : players) {
            Assert.assertFalse(playerStateManager.isAvailable(player));
            playerStateManager.markAvailable(player);
            Assert.assertTrue(playerStateManager.isAvailable(player));
        }

        playerStateManager.markBusy(players, session);

        Assert.assertFalse(playerStateManager.areAvailable(players));
        for (Long player : players) {
            Assert.assertFalse(playerStateManager.isAvailable(player));
            Assert.assertEquals(playerStateManager.getActiveSession(player), Long.valueOf(session));
        }
    }

    @Test
    public void testArbitraryListening() {
        long playerId = RANDOM.nextLong();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicLong expectedPlayer = new AtomicLong();
        final AtomicReference<PlayerState> expectedState = new AtomicReference<>();

        playerStateManager.subscribe(playerId, new MessageListener() {

            @Override
            public void onMessage(Message message, byte[] pattern) {
                try {
                    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
                    String deserializedChannel = stringSerializer.deserialize(message.getChannel());

                    expectedPlayer.set(Long.valueOf(deserializedChannel));
                    expectedState.set(PlayerState.values()[message.getBody()[0]]);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }
        });
        // There is a timeout between listen
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
        }

        playerStateManager.markAvailable(playerId);

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals("Message did not reach listener ", countDownLatch.getCount(), 0);
        Assert.assertEquals("Channel is incorrect ", expectedPlayer.get(), playerId);
        Assert.assertEquals("State is incorrect ", expectedState.get(), PlayerState.available);
    }

}
