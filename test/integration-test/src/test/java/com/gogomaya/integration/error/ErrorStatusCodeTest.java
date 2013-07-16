package com.gogomaya.integration.error;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.TicTacToe;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.tictactoe.TicTacToeSessionPlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { TestConfiguration.class })
public class ErrorStatusCodeTest {

    @Inject
    GameConstructionOperations<TicTacToeState> gameOperations;

    @Inject
    PlayerOperations playerOperations;
    
    @Inject
    GameScenarios gameScenarios;

    @Test(expected = GogomayaException.class)
    public void testSelectTwiceError() {
        List<GameSessionPlayer<TicTacToeState>> players = gameScenarios.constructGame(TicTacToe.NAME);
        TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);

        playerA.select(0, 0);
        playerA.select(1, 1);
    }

    @Test(expected = GogomayaException.class)
    public void testBetBig() {
        List<GameSessionPlayer<TicTacToeState>> players = gameScenarios.constructGame(TicTacToe.NAME);
        TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);

        playerA.select(0, 0);
        playerA.bet(1000);
    }

    @Test
    public void testCreatingSimultaniousGames() {
        Player playerA = playerOperations.createPlayer();

        GameSpecification specification = gameOperations.selectSpecification(playerA);

        GameSessionPlayer<TicTacToeState> gamePlayer = gameOperations.constructAutomatic(playerA, specification);
        GameSessionPlayer<TicTacToeState> anotherGamePlayer = gameOperations.constructAutomatic(playerA, specification);
        Assert.assertEquals(gamePlayer.getConstruction(), anotherGamePlayer.getConstruction());

        gamePlayer.close();
    }
/*
    @Test @Ignore
    public void testCreatingSimultaniousGamesWithGiveUp() {
        
        List<GameSessionPlayer<TicTacToeState>> players = gameScenarios.constructGame(TicTacToe.NAME);

        TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);
        TicTacToeSessionPlayer playerB = (TicTacToeSessionPlayer) players.get(1);

        Assert.assertEquals(playerA.getConstruction(), playerB.getConstruction());

        try {
            playerA.select(0, 0);
            playerA.bet(10);
            playerB.bet(10);
            playerA.giveUp();

            playerA = (TicTacToeSessionPlayer) gameOperations.constructAutomatic(playerA.getPlayer(), playerA.getSpecification());
            playerB = (TicTacToeSessionPlayer) gameOperations.constructAutomatic(playerB.getPlayer(), playerB.getSpecification());

            playerA.select(0, 0);
        } catch (Throwable cause) {
            cause.printStackTrace();
            throw new RuntimeException(cause);
        }
    }

    @Test @Ignore
    public void testScenario2() {
        Player playerA = playerOperations.createPlayer();

        Set<GogomayaFailure> errors = null;
        try {
            GameSpecification specification = gameOperations.selectSpecification(playerA);

            TicTacToeSessionPlayer gamePlayer = (TicTacToeSessionPlayer) gameOperations.constructAutomatic(playerA, specification);
            gamePlayer.giveUp();

            gamePlayer = (TicTacToeSessionPlayer) gameOperations.constructAutomatic(playerA, specification);
            gamePlayer.select(1, 1);
        } catch (GogomayaException gogomayaException) {
            errors = gogomayaException.getFailureDescription().getProblems();
        }

        Assert.assertEquals(errors.size(), 1);
        Assert.assertEquals(errors.iterator().next().getError(), GogomayaError.GamePlayGameNotStarted);
    }
    */
}
