package com.gogomaya.server.integration.error;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;
import com.gogomaya.server.spring.integration.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
public class ErrorStatusCodeTest {

    @Inject
    GameOperations<TicTacToeState> gameOperations;

    @Test(expected= GogomayaException.class)
    public void testSimpleStart() {
        List<GamePlayer<TicTacToeState>> players = gameOperations.start();
        TicTacToePlayer playerA = (TicTacToePlayer) players.get(0);

        playerA.select(0, 0);
        playerA.select(0, 0);
    }
}
