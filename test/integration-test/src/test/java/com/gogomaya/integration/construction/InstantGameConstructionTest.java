package com.gogomaya.integration.construction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.event.ExpectedAction;
import com.gogomaya.server.event.SimpleExpectedAction;
import com.gogomaya.server.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.game.tictactoe.TicTacToe;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles(value = SpringConfiguration.PROFILE_INTEGRATION_LOCAL_SERVER)
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { TestConfiguration.class })
public class InstantGameConstructionTest {

    @Autowired
    public GameConstructionOperations<TicTacToeState> constructionOperations;

    @Autowired
    public PlayerOperations playerOperations;

    @Test
    public void testResponceExtraction() {
        Player A = playerOperations.createPlayer();
        Player B = playerOperations.createPlayer();

        GameSessionPlayer<TicTacToeState> playerA = A.<TicTacToeState> getGameConstructor(TicTacToe.NAME).constructAvailability(A, B);
        ExpectedAction AtoAaction = constructionOperations.constructionResponse(A, A.getPlayerId(), playerA.getSession());
        ExpectedAction AtoBaction = constructionOperations.constructionResponse(A, B.getPlayerId(), playerA.getSession());

        ExpectedAction BtoAaction = constructionOperations.constructionResponse(B, A.getPlayerId(), playerA.getSession());
        ExpectedAction BtoBaction = constructionOperations.constructionResponse(B, B.getPlayerId(), playerA.getSession());

        assertEquals(AtoAaction, BtoAaction);
        assertEquals(AtoBaction, BtoBaction);
        assertTrue(AtoAaction instanceof InvitationAcceptedEvent);
        assertTrue(AtoBaction instanceof SimpleExpectedAction);
    }

}
