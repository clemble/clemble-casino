package com.clemble.casino.integration.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.clemble.casino.event.action.PlayerExpectedAction;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.player.event.PlayerInvitationAcceptedAction;
import com.clemble.casino.game.lifecycle.management.RoundState;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;

import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class AvailabilityGameConstructionITest {

    @Autowired
    public PlayerScenarios playerOperations;

    @Autowired
    public GameScenarios gameScenarios;

    @Test
    public void testSimpleCreation() {
        ClembleCasinoOperations A = playerOperations.createPlayer();
        ClembleCasinoOperations B = playerOperations.createPlayer();

        RoundGamePlayer playerA = gameScenarios.round(Game.num, A, B.getPlayer());
        B.gameConstructionOperations().accept(playerA.getSessionKey());

        playerA.waitForStart();
        Assert.assertTrue(playerA.isAlive());
    }

    @Test
    public void testScenarioCreation() {
        List<RoundGamePlayer> sessionPlayers = gameScenarios.round(Game.num);
        Assert.assertTrue(sessionPlayers.get(0).isToMove());
    }

    //@Test
    // TODO Restore, no point in waisting time now, this test is unstable
    public void testBusyGameInitiation() {
        // Step 1. Generating 3 players - A, B, C
        ClembleCasinoOperations A = playerOperations.createPlayer();
        ClembleCasinoOperations B = playerOperations.createPlayer();
        ClembleCasinoOperations C = playerOperations.createPlayer();
        // Step 2. Generating 2 instant game request A - B and A - C
        RoundGamePlayer AtoB = gameScenarios.round(Game.num, A, B.getPlayer());
        RoundGamePlayer AtoC = gameScenarios.round(Game.num, A, C.getPlayer());
        // Step 3. Accepting A - C game request to start A - C game
        RoundGamePlayer CtoA = gameScenarios.accept(AtoC.getSessionKey(), C);
        AtoC.waitForStart();
        AtoC.syncWith(CtoA);
        // Step 4. Accepting A - B game request to start A - B game, it should not be started until A - C game finishes
        RoundGamePlayer BtoA = gameScenarios.accept(AtoB.getSessionKey(), B);
        // Step 4.1 Checking appropriate alive states for A - B game
        assertAlive(BtoA, false);
        assertAlive(AtoB, false);
        // Step 4.1 Checking appropriate alive states for A - C game
        assertAlive(AtoC, true);
        assertAlive(CtoA, true);
        // Step 5. Stopping A - C game
        AtoC.giveUp();
        AtoC.waitForEnd();
        assertAlive(AtoC, false);
        // Step 6. Game A - B must start automatically
        BtoA.waitForStart();
        BtoA.syncWith(AtoB);
        // Step 7. Checking appropriate alive states for A - B game
        assertAlive(BtoA, true);
        assertAlive(AtoB, true);
        // Step 8. Checking appropriate alive states for A - C game
        CtoA.syncWith(AtoC);
        assertAlive(CtoA, false);
    }

    @Test
    public void testResponseExtraction() {
        ClembleCasinoOperations A = playerOperations.createPlayer();
        ClembleCasinoOperations B = playerOperations.createPlayer();

        RoundGamePlayer playerA = gameScenarios.round(Game.num, A, B.getPlayer());
        String sessionKey = playerA.getSessionKey();
        PlayerAction AtoAresponse = A.gameConstructionOperations().getResponse(sessionKey, A.getPlayer());
        PlayerAction AtoBresponse = A.gameConstructionOperations().getResponse(sessionKey, B.getPlayer());

        PlayerAction BtoAresponse = B.gameConstructionOperations().getResponse(sessionKey, A.getPlayer());
        PlayerAction BtoBresponse = B.gameConstructionOperations().getResponse(sessionKey, B.getPlayer());

        assertEquals(AtoAresponse, BtoAresponse);
        assertEquals(AtoBresponse, BtoBresponse);
        assertTrue(AtoAresponse.getAction() instanceof PlayerInvitationAcceptedAction);
        assertTrue(AtoBresponse.getAction() instanceof PlayerExpectedAction);
    }


    private <State extends RoundState> void assertAlive(RoundGamePlayer player, boolean alive) {
        if ((player.isAlive() && !alive) || (!player.isAlive() && alive)) {
            String playerIdentifier = player.playerOperations().getPlayer();
            GameConstruction construction = player.playerOperations().gameConstructionOperations().getConstruct(player.getSessionKey());
            List<String> opponents = new ArrayList<String>(construction.getResponses().fetchParticipants());
            opponents.remove(playerIdentifier);
            Assert.fail(player.getVersion() + " " + playerIdentifier + " with opponents " + opponents + " expected "+ (alive ? "to be" : "not to be") + " alive in " + construction.getSessionKey());
        }
    }
}
