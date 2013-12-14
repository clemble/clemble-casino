package com.clemble.casino.server.player.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.event.PlayerPresenceChangedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;
import com.clemble.test.random.ObjectGenerator;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PlayerCommonSpringConfiguration.class })
public class PlayerPresenceServerServiceTest {

    final private Logger LOG = LoggerFactory.getLogger(PlayerPresenceServerServiceTest.class);
    final private Random RANDOM = new Random();

    @Autowired
    public PlayerPresenceServerService playerPresenceService;

    @Autowired
    public SystemNotificationServiceListener presenceListenerService;

    @Test
    public void testMarkAvailable() {
        String player = String.valueOf(RANDOM.nextLong());

        assertFalse(playerPresenceService.isAvailable(player));

        playerPresenceService.markOnline(player);

        assertTrue(playerPresenceService.isAvailable(player));
    }

    @Test(expected = ClembleCasinoException.class)
    public void testMarkActiveIncorrect() {
        String player = String.valueOf(RANDOM.nextLong());
        String session = String.valueOf(RANDOM.nextLong());

        assertFalse(playerPresenceService.isAvailable(player));

        playerPresenceService.markPlaying(player, new GameSessionKey(Game.pic, session));
    }

    @Test
    public void testMarkActive() {
        String player = String.valueOf(RANDOM.nextLong());
        String session = String.valueOf(RANDOM.nextLong());

        assertFalse(playerPresenceService.isAvailable(player));

        playerPresenceService.markOnline(player);

        assertTrue(playerPresenceService.isAvailable(player));

        playerPresenceService.markPlaying(player, new GameSessionKey(Game.pic, session));

        assertFalse("Player must not be available", playerPresenceService.isAvailable(player));
        assertEquals("Player active session must match", playerPresenceService.getPresence(player).getSession().getSession(), session);
    }

    @Test
    public void testMarkCollectionActive() {
        Collection<String> players = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            players.add(String.valueOf(RANDOM.nextLong()));
        String session = String.valueOf(RANDOM.nextLong());

        for (String player : players) {
            assertFalse(playerPresenceService.isAvailable(player));
            playerPresenceService.markOnline(player);
            assertTrue(playerPresenceService.isAvailable(player));
        }

        assertTrue(playerPresenceService.markPlaying(players, new GameSessionKey(Game.pic, session)));

        assertFalse("None of the players supposed to be available ", playerPresenceService.areAvailable(players));
        for (String player : players) {
            assertFalse(playerPresenceService.isAvailable(player));
            assertEquals(playerPresenceService.getPresence(player).getSession().getSession(), session);
        }
    }

    /**
     * Tests that allocation of specific player is only possible once
     */
    final private int MARK_ACTIVE_IN_PARRALLEL_THREADS = 10;

    @Test
    public void testMarkActiveInParrallel() throws InterruptedException {
        final String genericPlayer = ObjectGenerator.generate(String.class);
        assertNotNull(playerPresenceService.markOnline(genericPlayer));
        assertTrue(playerPresenceService.isAvailable(genericPlayer));

        final CountDownLatch startLatch = new CountDownLatch(MARK_ACTIVE_IN_PARRALLEL_THREADS);
        final CountDownLatch endLatch = new CountDownLatch(MARK_ACTIVE_IN_PARRALLEL_THREADS);
        final AtomicInteger numLocksReceived = new AtomicInteger(0);

        final PlayerListener playerListener = new PlayerListener(1);
        presenceListenerService.subscribe(genericPlayer, playerListener);

        for (int i = 0; i < MARK_ACTIVE_IN_PARRALLEL_THREADS; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String randomSession = String.valueOf(RANDOM.nextLong());
                        List<String> participants = ImmutableList.<String> of();
                        try {
                            String anotherPlayer = ObjectGenerator.generate(String.class);
                            assertNotNull(playerPresenceService.markOnline(anotherPlayer));
                            assertTrue(playerPresenceService.isAvailable(anotherPlayer));

                            participants = ImmutableList.<String> of(genericPlayer, anotherPlayer);
                        } catch (Throwable throwable) {
                            LOG.error("Failed to process", throwable);
                        } finally {
                            startLatch.countDown();
                        }
                        startLatch.await(10, TimeUnit.SECONDS);

                        GameSessionKey sessionKey = new GameSessionKey(Game.pic, randomSession);
                        if (playerPresenceService.markPlaying(participants, sessionKey)) {
                            LOG.info("Success on mark playing with {}", sessionKey);
                            numLocksReceived.incrementAndGet();
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    } finally {
                        endLatch.countDown();
                    }
                }
            }).start();
        }

        endLatch.await(10, TimeUnit.SECONDS);
        assertEquals("Expected single lock, but got " + numLocksReceived.get(), 1, numLocksReceived.get());


        playerListener.countDownLatch.await(10, TimeUnit.SECONDS);

        assertEquals("Message did not reach listener ", playerListener.countDownLatch.getCount(), 0);
        Entry<String, Presence> calledPresence = playerListener.calls.poll();
        assertEquals("Channel is incorrect ", calledPresence.getKey(), genericPlayer);
        assertEquals("State is incorrect ", calledPresence.getValue(), Presence.playing);
    }

    @Test
    public void testArbitraryListening() throws InterruptedException {
        String player = ObjectGenerator.generate(String.class);

        PlayerListener playerListener = new PlayerListener(1);
        presenceListenerService.subscribe(player, playerListener);
        // There is a timeout between listen
        Thread.sleep(10);

        playerPresenceService.markOnline(player);

        playerListener.countDownLatch.await(1, TimeUnit.SECONDS);

        assertEquals("Message did not reach listener ", playerListener.countDownLatch.getCount(), 0);
        Entry<String, Presence> calledPresence = playerListener.calls.poll();
        assertEquals("Channel is incorrect ", calledPresence.getKey(), player);
        assertEquals("State is incorrect ", calledPresence.getValue(), Presence.online);
    }

    @Test
    public void testStateChangeListening() throws InterruptedException {
        String player = ObjectGenerator.generate(String.class);

        PlayerListener playerListener = new PlayerListener(3);
        presenceListenerService.subscribe(player, playerListener);
        // There is a timeout between listen

        Thread.sleep(50);

        playerPresenceService.markOnline(player);
        playerPresenceService.markPlaying(player, new GameSessionKey(Game.pic, String.valueOf(RANDOM.nextLong())));
        playerPresenceService.markOnline(player);

        playerListener.countDownLatch.await(1, TimeUnit.SECONDS);

        assertEquals("Message did not reach listener ", playerListener.countDownLatch.getCount(), 0);
        for (int i = 0; i < 3; i++) {
            Entry<String, Presence> call = playerListener.calls.poll();
            assertEquals("Channel is incorrect ", call.getKey(), player);
            assertEquals("State is incorrect ", call.getValue(), i % 2 == 0 ? Presence.online : Presence.playing);
        }
    }

    private class PlayerListener implements SystemEventListener<PlayerPresenceChangedEvent> {
        final public CountDownLatch countDownLatch;
        final public ArrayBlockingQueue<Entry<String, Presence>> calls;

        public PlayerListener(int numCalls) {
            countDownLatch = new CountDownLatch(numCalls);
            calls = new ArrayBlockingQueue<>(numCalls);
        }

        @Override
        public void onEvent(String player, PlayerPresenceChangedEvent state) {
            try {
                calls.put(new ImmutablePair<String, Presence>(player, state.getPresence()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }
    }

}
