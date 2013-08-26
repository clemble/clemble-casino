package com.gogomaya.integration.error;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.tictactoe.PicPacPoeSessionPlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.integration.PicPacPoeTestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.PicPacPoeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { PicPacPoeTestConfiguration.class })
public class ErrorStatusCodeTest {

    @Inject
    GameConstructionOperations<PicPacPoeState> gameOperations;

    @Inject
    PlayerOperations playerOperations;
    
    @Inject
    GameScenarios gameScenarios;

    @Test(expected = GogomayaException.class)
    public void testSelectTwiceError() {
        List<GameSessionPlayer<PicPacPoeState>> players = gameScenarios.constructGame(Game.pic);
        PicPacPoeSessionPlayer playerA = (PicPacPoeSessionPlayer) players.get(0);

        playerA.select(0, 0);
        playerA.select(1, 1);
    }

    @Test(expected = GogomayaException.class)
    public void testBetBig() {
        List<GameSessionPlayer<PicPacPoeState>> players = gameScenarios.constructGame(Game.pic);
        PicPacPoeSessionPlayer playerA = (PicPacPoeSessionPlayer) players.get(0);

        playerA.select(0, 0);
        playerA.bet(1000);
    }

    @Test
    public void testCreatingSimultaniousGames() {
        Player playerA = playerOperations.createPlayer();

        GameSpecification specification = gameOperations.selectSpecification(playerA);

        GameSessionPlayer<PicPacPoeState> gamePlayer = gameOperations.constructAutomatic(playerA, specification);
        GameSessionPlayer<PicPacPoeState> anotherGamePlayer = gameOperations.constructAutomatic(playerA, specification);
        assertEquals(gamePlayer.getSession(), anotherGamePlayer.getSession());

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
