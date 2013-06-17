package com.gogomaya.integration.tictactoe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.TicTacToeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class WalletOperationsTest {

    @Autowired
    private PlayerOperations playerOperations;

    @Autowired
    private GameOperations<TicTacToeState> gameOperations;

    @Test(expected = GogomayaException.class)
    public void runingOutOfMoney() {
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

}
