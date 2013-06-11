package com.gogomaya.integration.error;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.TicTacToeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { TestConfiguration.class })
public class ErrorStatusCodeTest {

    @Inject
    GameOperations<TicTacToeState> gameOperations;

    @Inject
    PlayerOperations playerOperations;

    @Test(expected = GogomayaException.class)
    public void testSelectTwiceError() {
        List<GamePlayer<TicTacToeState>> players = gameOperations.constructGame();
        TicTacToePlayer playerA = (TicTacToePlayer) players.get(0);

        playerA.select(0, 0);
        playerA.select(1, 1);
    }

    @Test(expected = GogomayaException.class)
    public void testBetBig() {
        List<GamePlayer<TicTacToeState>> players = gameOperations.constructGame();
        TicTacToePlayer playerA = (TicTacToePlayer) players.get(0);

        playerA.select(0, 0);
        playerA.bet(1000);
    }

    @Test(expected = GogomayaException.class)
    public void testOverTheLimit() {
        Player playerA = playerOperations.createPlayer();
        Player playerB = playerOperations.createPlayer();

        GameSpecification specification = gameOperations.selectSpecification();

        while (true) {
            TicTacToePlayer playerAState = (TicTacToePlayer) gameOperations.construct(playerA, specification);
            TicTacToePlayer playerBState = (TicTacToePlayer) gameOperations.construct(playerB, specification);

            if (playerAState.isToMove()) {
                playerAState.select(0, 0);
                playerAState.bet(1);
                playerBState.bet(1);
            } else {
                playerBState.select(0, 0);
                playerBState.bet(1);
                playerAState.bet(1);
            }

            playerAState.giveUp();

            playerAState.clear();
            playerBState.clear();
        }
    }

    @Test
    public void testCreatingSimultaniousGames() {
        Player playerA = playerOperations.createPlayer();

        GameSpecification specification = gameOperations.selectSpecification();

        GamePlayer<TicTacToeState> gamePlayer = gameOperations.construct(playerA, specification);
        GamePlayer<TicTacToeState> anotherGamePlayer = gameOperations.construct(playerA, specification);
        Assert.assertEquals(gamePlayer.getTableId(), anotherGamePlayer.getTableId());

        gamePlayer.clear();
    }

    @Test
    public void testCreatingSimultaniousGamesWithGiveUp() {
        List<GamePlayer<TicTacToeState>> players = gameOperations.constructGame();

        TicTacToePlayer playerA = (TicTacToePlayer) players.get(0);
        TicTacToePlayer playerB = (TicTacToePlayer) players.get(1);

        Assert.assertEquals(playerA.getTableId(), playerB.getTableId());

        try {
            playerA.select(0, 0);
            playerA.bet(10);
            playerB.bet(10);
            playerA.giveUp();

            playerA = (TicTacToePlayer) gameOperations.construct(playerA.getPlayer(), playerA.getSpecification());
            playerB = (TicTacToePlayer) gameOperations.construct(playerB.getPlayer(), playerB.getSpecification());

            playerA.select(0, 0);
        } finally {
            playerA.clear();
            playerB.clear();
        }
    }

    @Test
    public void testScenario2() {
        Player playerA = playerOperations.createPlayer();

        Set<GogomayaError> errors = null;
        try {
            GameSpecification specification = gameOperations.selectSpecification();

            TicTacToePlayer gamePlayer = (TicTacToePlayer) gameOperations.construct(playerA, specification);
            gamePlayer.giveUp();

            gamePlayer = (TicTacToePlayer) gameOperations.construct(playerA, specification);
            gamePlayer.select(1, 1);
            gamePlayer.clear();
        } catch (GogomayaException gogomayaException) {
            errors = gogomayaException.getFailure().getErrors();
        }

        Assert.assertEquals(errors.size(), 1);
        Assert.assertEquals(errors.iterator().next(), GogomayaError.GamePlayGameNotStarted);
    }
}