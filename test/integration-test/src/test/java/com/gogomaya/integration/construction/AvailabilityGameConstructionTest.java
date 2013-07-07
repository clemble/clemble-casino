package com.gogomaya.integration.construction;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.TicTacToe;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.construction.PlayerGameConstructionOperations;
import com.gogomaya.server.integration.game.tictactoe.TicTacToeSessionPlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { TestConfiguration.class })
public class AvailabilityGameConstructionTest {

    @Autowired
    PlayerOperations playerOperations;

    @Autowired
    GameConstructionOperations<?> gameOperations;

    @Autowired
    GameScenarios gameScenarios;

    @Test
    public void testSimpleCreation() {
        Player playerA = playerOperations.createPlayer();
        Player playerB = playerOperations.createPlayer();

        PlayerGameConstructionOperations<?> constructionOperations = playerA.<TicTacToeState> getGameConstructor(TicTacToe.NAME);
        GameSpecification specification = constructionOperations.selectSpecification();

        TicTacToeSessionPlayer sessionPlayer = (TicTacToeSessionPlayer) constructionOperations.constructAvailability(specification,
                ImmutableList.<Long> of(playerA.getPlayerId(), playerB.getPlayerId()));
        playerB.getGameConstructor(TicTacToe.NAME).acceptInvitation(sessionPlayer.getConstruction());

        sessionPlayer.waitForStart();
        Assert.assertTrue(sessionPlayer.isAlive());
    }

    @Test
    public void testScenarioCreation() {
        List<GameSessionPlayer<TicTacToeState>> sessionPlayers = gameScenarios.constructGame(TicTacToe.NAME);

        Assert.assertTrue(sessionPlayers.get(0).isToMove());
    }

    @Test
    @Ignore
    public void testBusyGameInteration() {
        // Step 1. Generating 3 players - A, B, C
        Player playerA = playerOperations.createPlayer();
        Player playerB = playerOperations.createPlayer();
        Player playerC = playerOperations.createPlayer();
        // Step 2. Generating 2 instant game request A - B and A - C
        TicTacToeSessionPlayer sessionABPlayer = (TicTacToeSessionPlayer) playerA.<TicTacToeState> getGameConstructor(TicTacToe.NAME).constructAvailability(
                ImmutableList.<Long> of(playerA.getPlayerId(), playerB.getPlayerId()));
        TicTacToeSessionPlayer sessionACPlayer = (TicTacToeSessionPlayer) playerA.<TicTacToeState> getGameConstructor(TicTacToe.NAME).constructAvailability(
                ImmutableList.<Long> of(playerA.getPlayerId(), playerC.getPlayerId()));
        // Step 3. Accepting A - C game request to start A - C game
        TicTacToeSessionPlayer sessionCAPlayer = (TicTacToeSessionPlayer) playerC.<TicTacToeState> getGameConstructor(TicTacToe.NAME).acceptInvitation(
                sessionACPlayer.getConstruction());
        sessionACPlayer.waitForStart();
        sessionACPlayer.syncWith(sessionCAPlayer);
        // Step 4. Accepting A - B game request to start A - B game, it should not be started until A - C game finishes
        TicTacToeSessionPlayer sessionBAPlayer = (TicTacToeSessionPlayer) playerB.<TicTacToeState> getGameConstructor(TicTacToe.NAME).acceptInvitation(
                sessionABPlayer.getConstruction());
        // Step 4.1 Checking appropriate alive states for A - B game
        assertLivenes(sessionBAPlayer, false);
        assertLivenes(sessionABPlayer, false);
        // Step 4.1 Checking appropriate alive states for A - C game
        assertLivenes(sessionACPlayer, true);
        assertLivenes(sessionCAPlayer, true);
        // Step 5. Stopping A - C game
        sessionACPlayer.giveUp();
        // Step 6. Game A - B must start automatically
        sessionBAPlayer.waitForStart();
        sessionBAPlayer.syncWith(sessionABPlayer);
        // Step 7. Checking appropriate alive states for A - B game
        assertLivenes(sessionBAPlayer, true);
        assertLivenes(sessionABPlayer, true);
        // Step 8. Checking appropriate alive states for A - C game
        assertLivenes(sessionACPlayer, false);
        assertLivenes(sessionCAPlayer, false);
    }

    private <State extends GameState> void assertLivenes(GameSessionPlayer<State> player, boolean alive) {
        if (player.isAlive() != alive) {
            long playerIdentifier = player.getPlayerId();
            List<Long> opponents = new ArrayList<>(player.getConstructionInfo().getResponces().fetchParticipants());
            opponents.remove(playerIdentifier);
            Assert.fail(playerIdentifier + " with opponents " + opponents + " expected " + (alive ? "to be" : "not to be") + " alive");
        }
    }
}
