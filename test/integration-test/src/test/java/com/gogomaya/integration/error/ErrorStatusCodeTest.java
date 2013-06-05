package com.gogomaya.integration.error;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;
import com.gogomaya.server.spring.integration.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("default")
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
    }
}
