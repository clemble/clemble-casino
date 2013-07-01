package com.gogomaya.integration.tictactoe;

import java.util.List;

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
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;

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
        List<GamePlayer<TicTacToeState>> players = gameOperations.constructGame();
        
        TicTacToePlayer playerAState = (TicTacToePlayer) players.get(0);
        Player playerA = playerAState.getPlayer();
        
        TicTacToePlayer playerBState = (TicTacToePlayer) players.get(1);
        Player playerB = players.get(1).getPlayer();

        GameSpecification specification = gameOperations.selectSpecification();

        do {

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

            playerAState = (TicTacToePlayer) gameOperations.construct(playerA, specification);
            playerBState = (TicTacToePlayer) gameOperations.construct(playerB, specification);
            
            playerAState.waitForStart();
            playerBState.waitForStart();
        } while(true);
    }

}
