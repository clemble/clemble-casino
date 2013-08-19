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

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.tictactoe.TicTacToeSessionPlayer;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.TicTacToeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class TicTacToeTimeoutTest {

    @Inject
    public GameScenarios gameOperations;

    @Test
    public void testMoveTimeout() {
        List<GameSessionPlayer<TicTacToeState>> players = gameOperations.constructGame(Game.pic);
        TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);
        TicTacToeSessionPlayer playerB = (TicTacToeSessionPlayer) players.get(1);
        GogomayaException gogomayaException = null;
        try {

            Assert.assertNotNull(players);
            Assert.assertEquals(playerA.getSession(), playerB.getSession());
            Assert.assertEquals(players.size(), 2);

            playerA.select(0, 0);

            sleep(3000);

            playerA.bet(1);
        } catch (GogomayaException exception) {
            gogomayaException = exception;
        } finally {
            playerA.close();
            playerB.close();
        }

        assertGogomayaFailure(gogomayaException, GogomayaError.GamePlayGameEnded);
    }

    @Test
    public void testTotalTimeout() {
        List<GameSessionPlayer<TicTacToeState>> players = gameOperations.constructGame(Game.pic);
        TicTacToeSessionPlayer playerA = (TicTacToeSessionPlayer) players.get(0);
        TicTacToeSessionPlayer playerB = (TicTacToeSessionPlayer) players.get(1);
        GogomayaException gogomayaException = null;

        Assert.assertTrue(playerA.getSpecification().getTotalTimeRule().getLimit() > 0);
        Assert.assertTrue(playerA.getSpecification().getMoveTimeRule().getLimit() > 0);

        int step = 0;
        long stepWaitTimeout = 300 + playerA.getSpecification().getTotalTimeRule().getLimit() / 4;

        try {

            Assert.assertNotNull(players);
            Assert.assertEquals(playerA.getSession(), playerB.getSession());
            Assert.assertEquals(players.size(), 2);

            playerA.select(0, 0);

            sleep(stepWaitTimeout);
            step++;

            playerA.bet(1);
            playerB.bet(1);

            sleep(stepWaitTimeout);
            step++;

            playerB.select(1, 0);

            sleep(stepWaitTimeout);
            step++;

            playerA.bet(1);
            playerB.bet(1);

            playerA.select(0, 1);

            sleep(stepWaitTimeout);
            step++;

            playerA.bet(1);
            playerB.bet(1);

        } catch (GogomayaException exception) {
            gogomayaException = exception;
        } finally {
            playerA.close();
            playerB.close();
        }

        // TODO this can happen due to time lags Assert.assertEquals(4, step);
        Assert.assertNotNull(gogomayaException);
        assertGogomayaFailure(gogomayaException, GogomayaError.GamePlayGameEnded);
    }

    public void assertGogomayaFailure(GogomayaException gogomayaException, GogomayaError error) {
        Assert.assertNotNull(gogomayaException);
        Assert.assertNotNull(gogomayaException.getFailureDescription());
        Assert.assertNotNull(gogomayaException.getFailureDescription().getProblems());
        Assert.assertEquals(gogomayaException.getFailureDescription().getProblems().size(), 1);
        Assert.assertEquals(gogomayaException.getFailureDescription().getProblems().iterator().next().getError(), error);

    }

    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
