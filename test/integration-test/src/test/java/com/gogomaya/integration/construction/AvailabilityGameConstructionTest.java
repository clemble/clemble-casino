package com.gogomaya.integration.construction;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

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
}
