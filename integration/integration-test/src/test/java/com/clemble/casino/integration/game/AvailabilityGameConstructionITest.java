package com.clemble.casino.integration.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.base.ExpectedEvent;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.schedule.InvitationAcceptedEvent;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.player.PlayerAware;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
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
        B.gameConstructionOperations().accept(playerA.getSession());

        playerA.waitForStart();
        Assert.assertTrue(playerA.isAlive());
    }

    @Test
    public void testScenarioCreation() {
        List<RoundGamePlayer<NumberState>> sessionPlayers = gameScenarios.round(Game.num);
        Assert.assertTrue(sessionPlayers.get(0).isToMove());
    }

    @Test
    public void testBusyGameInitiation() {
        // Step 1. Generating 3 players - A, B, C
        ClembleCasinoOperations A = playerOperations.createPlayer();
        ClembleCasinoOperations B = playerOperations.createPlayer();
        ClembleCasinoOperations C = playerOperations.createPlayer();
        // Step 2. Generating 2 instant game request A - B and A - C
        RoundGamePlayer AtoB = gameScenarios.round(Game.num, A, B.getPlayer());
        RoundGamePlayer AtoC = gameScenarios.round(Game.num, A, C.getPlayer());
        // Step 3. Accepting A - C game request to start A - C game
        RoundGamePlayer CtoA = gameScenarios.accept(AtoC.getSession(), C);
        AtoC.waitForStart();
        AtoC.syncWith(CtoA);
        // Step 4. Accepting A - B game request to start A - B game, it should not be started until A - C game finishes
        RoundGamePlayer BtoA = gameScenarios.accept(AtoB.getSession(), B);
        // Step 4.1 Checking appropriate alive states for A - B game
        assertLivenes(BtoA, false);
        assertLivenes(AtoB, false);
        // Step 4.1 Checking appropriate alive states for A - C game
        assertLivenes(AtoC, true);
        assertLivenes(CtoA, true);
        // Step 5. Stopping A - C game
        AtoC.giveUp();
        AtoC.waitForEnd();
        assertLivenes(AtoC, false);
        // Step 6. Game A - B must start automatically
        BtoA.waitForStart();
        BtoA.syncWith(AtoB);
        // Step 7. Checking appropriate alive states for A - B game
        assertLivenes(BtoA, true);
        assertLivenes(AtoB, true);
        // Step 8. Checking appropriate alive states for A - C game
        CtoA.syncWith(AtoC);
        assertLivenes(CtoA, false);
    }

    @Test
    public void testResponseExtraction() {
        ClembleCasinoOperations A = playerOperations.createPlayer();
        ClembleCasinoOperations B = playerOperations.createPlayer();

        RoundGamePlayer playerA = gameScenarios.round(Game.num, A, B.getPlayer());
        GameSessionKey sessionKey = playerA.getSession();
        PlayerAware AtoAresponse = A.gameConstructionOperations().getResponce(sessionKey, A.getPlayer());
        PlayerAware AtoBresponse = A.gameConstructionOperations().getResponce(sessionKey, B.getPlayer());

        PlayerAware BtoAresponse = B.gameConstructionOperations().getResponce(sessionKey, A.getPlayer());
        PlayerAware BtoBresponse = B.gameConstructionOperations().getResponce(sessionKey, B.getPlayer());

        assertEquals(AtoAresponse, BtoAresponse);
        assertEquals(AtoBresponse, BtoBresponse);
        assertTrue(AtoAresponse instanceof InvitationAcceptedEvent);
        assertTrue(AtoBresponse instanceof ExpectedEvent);
    }


    private <State extends GameState> void assertLivenes(RoundGamePlayer<State> player, boolean alive) {
        if ((player.isAlive() && !alive) || (!player.isAlive() && alive)) {
            String playerIdentifier = player.playerOperations().getPlayer();
            GameConstruction construction = player.playerOperations().gameConstructionOperations().getConstruct(player.getSession());
            List<String> opponents = new ArrayList<String>(construction.getResponses().fetchParticipants());
            opponents.remove(playerIdentifier);
            Assert.fail(player.getState().getVersion() + " " + playerIdentifier + " with opponents " + opponents + " expected "+ (alive ? "to be" : "not to be") + " alive in " + construction.getSession());
        }
    }
}
