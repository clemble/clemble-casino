package com.gogomaya.integration.tictactoe;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.construction.PlayerGameConstructionOperations;
import com.gogomaya.server.integration.game.tictactoe.PicPacPoeSessionPlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.integration.PicPacPoeTestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.PicPacPoeState;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PicPacPoeTestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class WalletOperationsTest {

    @Autowired
    private PlayerOperations playerOperations;

    @Autowired
    private GameScenarios gameOperations;

    @Test(expected = GogomayaException.class)
    public void runingOutOfMoney() {
        Player playerA = playerOperations.createPlayer();
        Player playerB = playerOperations.createPlayer();

        Collection<Long> participants = ImmutableList.of(playerA.getPlayerId(), playerB.getPlayerId());

        PlayerGameConstructionOperations<PicPacPoeState> playerAConstructionOp = playerA.getGameConstructor(Game.pic);
        PlayerGameConstructionOperations<PicPacPoeState> playerBConstructionOp = playerB.getGameConstructor(Game.pic);

        do {
            PicPacPoeSessionPlayer sessionAPlayer = (PicPacPoeSessionPlayer) playerAConstructionOp.constructAvailability(
                    playerAConstructionOp.selectSpecification(), participants);
            PicPacPoeSessionPlayer sessionBPlayer = (PicPacPoeSessionPlayer) playerBConstructionOp.acceptInvitation(sessionAPlayer.getSession());

            sessionAPlayer.waitForStart();
            sessionBPlayer.waitForStart();

            if (sessionAPlayer.isToMove()) {
                sessionAPlayer.select(0, 0);
                sessionAPlayer.bet(1);
                sessionBPlayer.bet(1);
            } else {
                sessionBPlayer.select(0, 0);
                sessionBPlayer.bet(1);
                sessionAPlayer.bet(1);
            }

            sessionAPlayer.giveUp();
        } while (true);
    }

}
