package com.clemble.casino.server.player.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.player.notification.PlayerNotificationListener;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;
import com.google.common.collect.ImmutableList;

@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PlayerCommonSpringConfiguration.class })
public class PlayerPresenceServerServiceTest {

    final private Random RANDOM = new Random();

    @Autowired
    public PlayerPresenceServerService playerPresenceService;

    @Test
    public void testMarkAvailable() {
        String player = String.valueOf(RANDOM.nextLong());

        Assert.assertFalse(playerPresenceService.isAvailable(player));

        playerPresenceService.markOnline(player);

        Assert.assertTrue(playerPresenceService.isAvailable(player));
    }

    @Test(expected = ClembleCasinoException.class)
    public void testMarkActiveIncorrect() {
        String player = String.valueOf(RANDOM.nextLong());
        long session = RANDOM.nextLong();

        Assert.assertFalse(playerPresenceService.isAvailable(player));

        playerPresenceService.markPlaying(player, new GameSessionKey(Game.pic, session));
    }

    @Test
    public void testMarkActive() {
        String player = String.valueOf(RANDOM.nextLong());
        long session = RANDOM.nextLong();

        Assert.assertFalse(playerPresenceService.isAvailable(player));

        playerPresenceService.markOnline(player);

        Assert.assertTrue(playerPresenceService.isAvailable(player));

        playerPresenceService.markPlaying(player, new GameSessionKey(Game.pic, session));

        Assert.assertFalse("Player must not be available", playerPresenceService.isAvailable(player));
        Assert.assertEquals("Player active session must match", playerPresenceService.getPresence(player).getSession().getSession(), session);
    }

    @Test
    public void testMarkCollectionActive() {
        Collection<String> players = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            players.add(String.valueOf(RANDOM.nextLong()));
        long session = RANDOM.nextLong();

        for (String player : players) {
            Assert.assertFalse(playerPresenceService.isAvailable(player));
            playerPresenceService.markOnline(player);
            Assert.assertTrue(playerPresenceService.isAvailable(player));
        }

        playerPresenceService.markPlaying(players, new GameSessionKey(Game.pic, session));

        Assert.assertFalse("None of the players supposed to be available ", playerPresenceService.areAvailable(players));
        for (String player : players) {
            Assert.assertFalse(playerPresenceService.isAvailable(player));
            Assert.assertEquals(playerPresenceService.getPresence(player).getSession().getSession(), session);
        }
    }

    /**
     * Tests that allocation of specific player is only possible once
     */
    final private int MARK_ACTIVE_IN_PARRALLEL_THREADS = 100;

    @Test
    public void testMarkActiveInParrallel() {
        final String genericPlayer = String.valueOf(RANDOM.nextLong());
        playerPresenceService.markOnline(genericPlayer);
        Assert.assertTrue(playerPresenceService.isAvailable(genericPlayer));

        final CountDownLatch startLatch = new CountDownLatch(MARK_ACTIVE_IN_PARRALLEL_THREADS);
        final CountDownLatch endLatch = new CountDownLatch(MARK_ACTIVE_IN_PARRALLEL_THREADS);
        final AtomicInteger numLocksReceived = new AtomicInteger(0);

        final PlayerListener playerListener = new PlayerListener(1);
        playerPresenceService.subscribe(genericPlayer, playerListener);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
        }

        for (int i = 0; i < MARK_ACTIVE_IN_PARRALLEL_THREADS; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String anotherPlayer = String.valueOf(RANDOM.nextLong());
                        playerPresenceService.markOnline(anotherPlayer);
                        Assert.assertTrue(playerPresenceService.isAvailable(anotherPlayer));

                        List<String> participants = ImmutableList.<String> of(genericPlayer, anotherPlayer);
                        long randomSession = RANDOM.nextLong();
                        startLatch.countDown();
                        startLatch.await();

                        if (playerPresenceService.markPlaying(participants, new GameSessionKey(Game.pic, randomSession)))
                            numLocksReceived.incrementAndGet();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    } finally {
                        endLatch.countDown();
                    }
                }
            }).start();
            ;
        }

        try {
            endLatch.await();
        } catch (InterruptedException e) {
        }
        Assert.assertEquals("Expected single lock, but got " + numLocksReceived.get(), 1, numLocksReceived.get());

        try {
            playerListener.countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals("Message did not reach listener ", playerListener.countDownLatch.getCount(), 0);
        Assert.assertEquals("Channel is incorrect ", playerListener.expectedPlayer.poll(), genericPlayer);
        Assert.assertEquals("State is incorrect ", playerListener.expectedState.poll(), Presence.playing);
    }

    @Test
    public void testArbitraryListening() throws InterruptedException {
        String player = String.valueOf(RANDOM.nextLong());

        PlayerListener playerListener = new PlayerListener(1);
        playerPresenceService.subscribe(player, playerListener);
        // There is a timeout between listen
        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
        }

        playerPresenceService.markOnline(player);

        playerListener.countDownLatch.await(1, TimeUnit.SECONDS);

        Assert.assertEquals("Message did not reach listener ", playerListener.countDownLatch.getCount(), 0);
        Assert.assertEquals("Channel is incorrect ", playerListener.expectedPlayer.poll(), player);
        Assert.assertEquals("State is incorrect ", playerListener.expectedState.poll(), Presence.online);
    }

    @Test
    public void testStateChangeListening() throws InterruptedException {
        String player = String.valueOf(RANDOM.nextLong());

        PlayerListener playerListener = new PlayerListener(3);
        playerPresenceService.subscribe(player, playerListener);
        // There is a timeout between listen

        Thread.sleep(50);

        playerPresenceService.markOnline(player);
        playerPresenceService.markPlaying(player, new GameSessionKey(Game.pic, RANDOM.nextLong()));
        playerPresenceService.markOnline(player);

        playerListener.countDownLatch.await(1, TimeUnit.SECONDS);

        Assert.assertEquals("Message did not reach listener ", playerListener.countDownLatch.getCount(), 0);
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals("Channel is incorrect ", playerListener.expectedPlayer.poll(), player);
            Assert.assertEquals("State is incorrect ", playerListener.expectedState.poll(), i % 2 == 0 ? Presence.online : Presence.playing);
        }
    }

    private class PlayerListener implements PlayerNotificationListener<Presence> {
        final public CountDownLatch countDownLatch;
        final public ArrayBlockingQueue<String> expectedPlayer;
        final public ArrayBlockingQueue<Presence> expectedState;

        public PlayerListener(int numCalls) {
            countDownLatch = new CountDownLatch(numCalls);
            expectedPlayer = new ArrayBlockingQueue<>(numCalls);
            expectedState = new ArrayBlockingQueue<>(numCalls);
        }

        @Override
        public void onUpdate(String player, Presence state) {
            try {
                expectedPlayer.put(player);
                expectedState.put(state);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }
    }

}
