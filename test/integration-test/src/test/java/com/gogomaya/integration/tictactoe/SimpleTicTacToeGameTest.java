package com.gogomaya.integration.tictactoe;

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

import com.gogomaya.server.ExpectedAction;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.outcome.PlayerWonOutcome;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.tictactoe.TicTacToeSessionPlayer;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class SimpleTicTacToeGameTest {

    @Inject
    GameScenarios gameScenarios;

    @Inject
    GameConstructionOperations<TicTacToeState> gameOperations;

    @Test
    public void testMoneyAndMoveProcessing() {
        List<GameSessionPlayer<TicTacToeState>> players = gameScenarios.constructGame(Game.pic);
        TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);
        TicTacToeSessionPlayer playerB = (TicTacToeSessionPlayer) players.get(1);
        try {
            long gamePrice = playerA.getSpecification().getPrice().getAmount();

            playerA.select(0, 0);

            playerA.bet(5);
            playerB.bet(2);

            playerB.isToMove();
            Assert.assertEquals(playerB.getNextMove().getClass(), ExpectedAction.class);
            playerA.syncWith(playerB);

            Assert.assertTrue(playerB.getState().getBoard()[0][0].owned());
            Assert.assertEquals(playerB.getMoneyLeft(), gamePrice - 2);
            Assert.assertEquals(playerA.getMoneyLeft(), gamePrice - 5);
        } finally {
            playerA.close();
            playerB.close();
        }
    }

    @Test
    public void testSimpleScenario() {
        List<GameSessionPlayer<TicTacToeState>> players = gameScenarios.constructGame(Game.pic);
        TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);
        TicTacToeSessionPlayer playerB = (TicTacToeSessionPlayer) players.get(1);

        Currency currency = playerA.getSpecification().getPrice().getCurrency();
        Assert.assertEquals(playerA.getSpecification().getPrice(), playerB.getSpecification().getPrice());
        long gamePrice = playerA.getSpecification().getPrice().getAmount();
        long originalAmount = playerA.getPlayer().getWalletOperations().getAccount().getMoney(currency).getAmount();

        try {
            playerA.syncWith(playerB);
            playerA.select(0, 0);

            playerA.bet(2);
            Assert.assertEquals(playerA.getMoneyLeft(), gamePrice);
            Assert.assertEquals(playerB.getMoneyLeft(), gamePrice);
            playerB.bet(1);
            playerB.syncWith(playerA);

            playerB.select(1, 1);

            playerB.bet(1);
            playerB.syncWith(playerA);
            playerA.bet(2);

            playerA.select(2, 2);

            playerA.bet(2);
            playerA.syncWith(playerB);
            playerB.bet(1);
            playerB.syncWith(playerA);

            Assert.assertEquals(playerB.getPlayer().getWalletOperations().getAccount().getMoney(currency).getAmount(), originalAmount - gamePrice);
            Assert.assertEquals(playerA.getPlayer().getWalletOperations().getAccount().getMoney(currency).getAmount(), originalAmount + gamePrice);

            playerA.syncWith(playerB);

            Assert.assertTrue(playerB.getState().complete());
            Assert.assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());
        } finally {
            playerA.close();
            playerB.close();
        }
    }

    @Test
    public void testScenarioRow() {
        for (int row = 0; row < 3; row++) {
            List<GameSessionPlayer<TicTacToeState>> players = gameScenarios.constructGame(Game.pic);
            TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);
            TicTacToeSessionPlayer playerB = (TicTacToeSessionPlayer) players.get(1);
            try {
                playerA.select(0, row);
                playerA.bet(2);
                playerB.bet(1);

                playerB.select(1, row);
                playerB.bet(1);
                playerA.bet(2);

                playerA.select(2, row);
                playerA.bet(2);
                playerB.bet(1);

                Assert.assertTrue(playerB.getState().complete());
                Assert.assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());
            } finally {
                playerA.close();
                playerB.close();
            }
        }
    }

    @Test
    public void testScenarioColumn() {
        for (int column = 0; column < 3; column++) {
            List<GameSessionPlayer<TicTacToeState>> players = gameScenarios.constructGame(Game.pic);
            TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);
            TicTacToeSessionPlayer playerB = (TicTacToeSessionPlayer) players.get(1);
            try {
                playerA.select(column, 0);
                playerA.bet(2);
                playerB.bet(1);

                playerB.select(column, 1);
                playerB.bet(1);
                playerA.bet(2);

                playerA.select(column, 2);
                playerA.bet(2);
                playerB.bet(1);

                Assert.assertTrue(playerB.getState().complete());
                Assert.assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());
            } finally {
                playerA.close();
                playerB.close();
            }
        }
    }

    @Test
    public void scenario1() {
        List<GameSessionPlayer<TicTacToeState>> players = gameScenarios.constructGame(Game.pic);
        TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);
        TicTacToeSessionPlayer playerB = (TicTacToeSessionPlayer) players.get(1);
        try {
            playerA.select(0, 0);
            playerA.bet((int) playerA.getMoneyLeft());
            playerB.bet(1);

            Assert.assertTrue(playerB.getState().complete());
            Assert.assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerB.getPlayer().getPlayerId());
        } finally {
            playerA.close();
            playerB.close();
        }
    }

}
