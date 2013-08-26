package com.gogomaya.integration.tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

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
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.tictactoe.PicPacPoeSessionPlayer;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.spring.integration.PicPacPoeTestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.PicPacPoeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PicPacPoeTestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class SimpleTicTacToeGameTest {

    @Inject
    GameScenarios gameScenarios;

    @Inject
    GameConstructionOperations<PicPacPoeState> gameOperations;

    @Test
    public void testMoneyAndMoveProcessing() {
        List<GameSessionPlayer<PicPacPoeState>> players = gameScenarios.constructGame(Game.pic);
        PicPacPoeSessionPlayer playerA = (PicPacPoeSessionPlayer) players.get(0);
        PicPacPoeSessionPlayer playerB = (PicPacPoeSessionPlayer) players.get(1);
        try {
            long gamePrice = playerA.getSpecification().getPrice().getAmount();

            playerA.select(0, 0);

            playerA.bet(5);
            playerB.bet(2);

            playerB.isToMove();
            assertEquals(playerB.getNextMove().getClass(), ExpectedAction.class);
            playerA.syncWith(playerB);

            assertTrue(playerB.getState().getBoard().owned(0, 0));
            assertEquals(playerB.getMoneyLeft(), gamePrice - 2);
            assertEquals(playerA.getMoneyLeft(), gamePrice - 5);
        } finally {
            playerA.close();
            playerB.close();
        }
    }

    @Test
    public void testSimpleScenario() {
        List<GameSessionPlayer<PicPacPoeState>> players = gameScenarios.constructGame(Game.pic);
        PicPacPoeSessionPlayer playerA = (PicPacPoeSessionPlayer) players.get(0);
        PicPacPoeSessionPlayer playerB = (PicPacPoeSessionPlayer) players.get(1);

        Currency currency = playerA.getSpecification().getPrice().getCurrency();
        assertEquals(playerA.getSpecification().getPrice(), playerB.getSpecification().getPrice());
        long gamePrice = playerA.getSpecification().getPrice().getAmount();
        long originalAmount = playerA.getPlayer().getWalletOperations().getAccount().getMoney(currency).getAmount();

        try {
            playerA.syncWith(playerB);
            playerA.select(0, 0);

            playerA.bet(2);
            assertEquals(playerA.getMoneyLeft(), gamePrice);
            assertEquals(playerB.getMoneyLeft(), gamePrice);
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

            assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());

            assertEquals(playerB.getPlayer().getWalletOperations().getAccount().getMoney(currency).getAmount(), originalAmount - gamePrice);
            assertEquals(playerA.getPlayer().getWalletOperations().getAccount().getMoney(currency).getAmount(), originalAmount + gamePrice);
        } finally {
            playerA.close();
            playerB.close();
        }
    }

    @Test
    public void testScenarioRow() {
        for (int row = 0; row < 3; row++) {
            List<GameSessionPlayer<PicPacPoeState>> players = gameScenarios.constructGame(Game.pic);
            PicPacPoeSessionPlayer playerA = (PicPacPoeSessionPlayer) players.get(0);
            PicPacPoeSessionPlayer playerB = (PicPacPoeSessionPlayer) players.get(1);
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

                assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());
            } finally {
                playerA.close();
                playerB.close();
            }
        }
    }

    @Test
    public void testScenarioColumn() {
        for (int column = 0; column < 3; column++) {
            List<GameSessionPlayer<PicPacPoeState>> players = gameScenarios.constructGame(Game.pic);
            PicPacPoeSessionPlayer playerA = (PicPacPoeSessionPlayer) players.get(0);
            PicPacPoeSessionPlayer playerB = (PicPacPoeSessionPlayer) players.get(1);
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

                assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());
            } finally {
                playerA.close();
                playerB.close();
            }
        }
    }

}
