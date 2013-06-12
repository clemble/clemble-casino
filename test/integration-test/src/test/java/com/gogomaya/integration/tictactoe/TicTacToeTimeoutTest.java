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
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.TicTacToeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class TicTacToeTimeoutTest {

    @Inject
    public GameOperations<TicTacToeState> gameOperations;

    @Test
    public void testMoveTimeout() {
        List<GamePlayer<TicTacToeState>> players = gameOperations.constructGame();
        TicTacToePlayer playerA = (TicTacToePlayer) players.get(0);
        TicTacToePlayer playerB = (TicTacToePlayer) players.get(1);
        GogomayaException gogomayaException = null;
        try {

            Assert.assertNotNull(players);
            Assert.assertEquals(playerA.getTableId(), playerB.getTableId());
            Assert.assertEquals(players.size(), 2);

            playerA.select(0, 0);

            sleep(3000);

            playerA.bet(1);
        } catch (GogomayaException exception) {
            gogomayaException = exception;
        } finally {
            playerA.clear();
            playerB.clear();
        }

        assertGogomayaFailure(gogomayaException, GogomayaError.GamePlayGameEnded);
    }

    @Test
    public void testTotalTimeout() {
        List<GamePlayer<TicTacToeState>> players = gameOperations.constructGame();
        TicTacToePlayer playerA = (TicTacToePlayer) players.get(0);
        TicTacToePlayer playerB = (TicTacToePlayer) players.get(1);
        GogomayaException gogomayaException = null;

        Assert.assertTrue(playerA.getSpecification().getTotalTimeRule().getLimit() > 0);
        Assert.assertTrue(playerA.getSpecification().getMoveTimeRule().getLimit() > 0);

        int step = 0;
        try {

            Assert.assertNotNull(players);
            Assert.assertEquals(playerA.getTableId(), playerB.getTableId());
            Assert.assertEquals(players.size(), 2);

            playerA.select(0, 0);

            sleep(1200);
            step++;

            playerA.bet(1);
            playerB.bet(1);

            sleep(1200);
            step++;

            playerB.select(1, 0);

            sleep(1200);
            step++;

            playerA.bet(1);
            playerB.bet(1);

            playerA.select(0, 1);

            sleep(1200);
            step++;

            playerA.bet(1);

        } catch (GogomayaException exception) {
            gogomayaException = exception;
        } finally {
            playerA.clear();
            playerB.clear();
        }

        Assert.assertEquals(4, step);
        assertGogomayaFailure(gogomayaException, GogomayaError.GamePlayGameEnded);
    }

    public void assertGogomayaFailure(GogomayaException gogomayaException, GogomayaError error) {
        Assert.assertNotNull(gogomayaException);
        Assert.assertNotNull(gogomayaException.getFailure());
        Assert.assertNotNull(gogomayaException.getFailure().getErrors());
        Assert.assertEquals(gogomayaException.getFailure().getErrors().size(), 1);
        Assert.assertEquals(gogomayaException.getFailure().getErrors().iterator().next(), error);

    }

    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
