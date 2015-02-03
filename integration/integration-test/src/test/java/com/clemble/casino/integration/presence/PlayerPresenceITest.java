package com.clemble.casino.integration.presence;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.integration.ClembleIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.game.Game;
import com.clemble.casino.integration.game.RoundGamePlayer;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.event.PlayerPresenceChangedEvent;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class PlayerPresenceITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired
    public GameScenarios gameScenarios;

    @Test
    public void testPresenceRead() {
        // Step 1. Creating player A & B
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        try {
            // Step 2. Checking A presence of A is the same for A & B
            assertEquals(A.presenceOperations().myPresence(), A.presenceOperations().getPresence(A.getPlayer()));
            assertEquals(A.presenceOperations().myPresence(), B.presenceOperations().getPresence(A.getPlayer()));
            // Step 2. Checking B presence of B is the same for A & B
            assertEquals(B.presenceOperations().myPresence(), B.presenceOperations().getPresence(B.getPlayer()));
            assertEquals(B.presenceOperations().myPresence(), A.presenceOperations().getPresence(B.getPlayer()));
            A.presenceOperations().getPresences(A.getPlayer(), B.getPlayer());
        } finally {
            A.close();
            B.close();
        }
    }

    @Test
    public void testPresenceUpdate() throws InterruptedException {
        // Step 1. Creating player A & B
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        ClembleCasinoOperations C = playerScenarios.createPlayer();
        try {
            // Step 2. Checking A presence of A is the same for A & B
            assertEquals(A.presenceOperations().myPresence(), A.presenceOperations().getPresence(A.getPlayer()));
            assertEquals(A.presenceOperations().myPresence(), B.presenceOperations().getPresence(A.getPlayer()));
            assertEquals(A.presenceOperations().myPresence(), C.presenceOperations().getPresence(A.getPlayer()));
            // Step 2. Checking B presence of B is the same for A & B
            assertEquals(B.presenceOperations().myPresence(), A.presenceOperations().getPresence(B.getPlayer()));
            assertEquals(B.presenceOperations().myPresence(), B.presenceOperations().getPresence(B.getPlayer()));
            assertEquals(B.presenceOperations().myPresence(), C.presenceOperations().getPresence(B.getPlayer()));
            // Step 3. Checking B presence of B is the same for A & B
            assertEquals(C.presenceOperations().myPresence(), C.presenceOperations().getPresence(C.getPlayer()));
            assertEquals(C.presenceOperations().myPresence(), C.presenceOperations().getPresence(C.getPlayer()));
            assertEquals(C.presenceOperations().myPresence(), C.presenceOperations().getPresence(C.getPlayer()));
            // Step 4. Listening for presence updates from B and C
            final BlockingQueue<PlayerPresence> BtoApresence = new ArrayBlockingQueue<>(3);
            A.listenerOperations().subscribeToPresenceEvents(B.getPlayer(), new EventListener<PlayerPresenceChangedEvent>() {
                @Override
                public void onEvent(PlayerPresenceChangedEvent event) {
                    BtoApresence.add(event);
                }
            });
            final BlockingQueue<PlayerPresence> CtoApresence = new ArrayBlockingQueue<>(5);
            A.listenerOperations().subscribeToPresenceEvents(C.getPlayer(), new EventListener<PlayerPresenceChangedEvent>() {
                @Override
                public void onEvent(PlayerPresenceChangedEvent event) {
                    CtoApresence.add((PlayerPresence) event);
                }
            });
            // Step 5. Initiating a game between B and C
            RoundGamePlayer BtoC = gameScenarios.round(Game.num, B, C.getPlayer());
            RoundGamePlayer CtoB = gameScenarios.accept(BtoC.getSessionKey(), C);
            // Step 6. Checking presences
            PlayerPresence Bpresence = BtoApresence.poll(15, TimeUnit.SECONDS);
            PlayerPresence Cpresence = CtoApresence.poll(15, TimeUnit.SECONDS);
            // Step 7. Checking received same session for A and C
            assertEquals(Bpresence.getSessionKey(), BtoC.getSessionKey());
            assertEquals(Cpresence.getSessionKey(), CtoB.getSessionKey());
            assertEquals(BtoApresence.size(), 0);
            assertEquals(CtoApresence.size(), 0);
        } finally {
            A.close();
            B.close();
            C.close();
        }
    }

}
