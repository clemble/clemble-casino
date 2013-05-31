package com.gogomaya.integration.tictactoe;

import java.util.List;

import javax.inject.Inject;

import org.jbehave.core.annotations.UsingSteps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.game.action.PlayerWonOutcome;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.tictactoe.TicTacToeOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.tests.validation.PlayerCredentialsValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@UsingSteps(instances = PlayerCredentialsValidation.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class SimpleTicTacToeGameTest {

    @Inject
    TicTacToeOperations ticTacToeOperations;

    @Inject
    GameOperations<TicTacToeState> gameOperations;

    @Test
    public void testSimpleStart() {
        List<GamePlayer<TicTacToeState>> players = gameOperations.constructGame();
        TicTacToePlayer playerA = (TicTacToePlayer) players.get(0);
        TicTacToePlayer playerB = (TicTacToePlayer) players.get(1);

        int gamePrice = playerA.getSpecification().getBetRule().getPrice();

        long playerAidentifier = playerA.getPlayer().getPlayerId();
        long playerBidentifier = playerB.getPlayer().getPlayerId();

        Assert.assertNotNull(players);
        Assert.assertEquals(playerA.getTableId(), playerB.getTableId());
        Assert.assertEquals(players.size(), 2);

        playerA.select(0, 0);

        playerA.bet(5);
        playerB.bet(2);

        Assert.assertTrue(playerB.getState().getBoard()[0][0].owned());
        Assert.assertEquals(playerB.getState().getNextMoves().size(), 1);
        Assert.assertEquals(playerB.getState().getPlayerState(playerAidentifier).getMoneyLeft(), gamePrice - 5);
        Assert.assertEquals(playerB.getState().getPlayerState(playerBidentifier).getMoneyLeft(), gamePrice - 2);
        Assert.assertNotNull(playerB.getState().getNextMove(playerBidentifier));
    }

    @Test
    public void testSimpleScenario() {
        List<GamePlayer<TicTacToeState>> players = gameOperations.constructGame();
        TicTacToePlayer playerA = (TicTacToePlayer) players.get(0);
        TicTacToePlayer playerB = (TicTacToePlayer) players.get(1);

        Money gamePrice = Money.create(playerA.getSpecification().getCurrency(), playerA.getSpecification().getBetRule().getPrice());
        Money originalAmount = playerA.getPlayer().getWallet().getMoney(gamePrice.getCurrency());

        try {
            playerA.syncWith(playerB);
            playerA.select(0, 0);

            playerA.bet(2);
            Assert.assertEquals(playerA.getState().getPlayerState(playerA.getPlayer().getPlayerId()).getMoneyLeft(), gamePrice.getAmount());
            Assert.assertEquals(playerA.getState().getPlayerState(playerB.getPlayer().getPlayerId()).getMoneyLeft(), gamePrice.getAmount());
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

            Assert.assertEquals(playerB.getPlayer().getWallet().getMoney(gamePrice.getCurrency()), originalAmount.subtract(gamePrice));
            Assert.assertEquals(playerA.getPlayer().getWallet().getMoney(gamePrice.getCurrency()), originalAmount.add(gamePrice));

            playerA.syncWith(playerB);

            Assert.assertTrue(playerB.getState().complete());
            Assert.assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());
        } finally {
            playerA.clear();
            playerB.clear();
        }
    }
}
