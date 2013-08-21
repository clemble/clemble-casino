package com.gogomaya.integration.construction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.ExpectedAction;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.PicPacPoeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { TestConfiguration.class })
public class InstantGameConstructionTest {

    @Autowired
    public GameConstructionOperations<PicPacPoeState> constructionOperations;

    @Autowired
    public PlayerOperations playerOperations;

    @Test
    public void testResponseExtraction() {
        Player A = playerOperations.createPlayer();
        Player B = playerOperations.createPlayer();

        GameSessionPlayer<PicPacPoeState> playerA = A.<PicPacPoeState> getGameConstructor(Game.pic).constructAvailability(A, B);
        PlayerAware AtoAaction = constructionOperations.constructionResponse(A, A.getPlayerId(), playerA.getSession());
        PlayerAware AtoBaction = constructionOperations.constructionResponse(A, B.getPlayerId(), playerA.getSession());

        PlayerAware BtoAaction = constructionOperations.constructionResponse(B, A.getPlayerId(), playerA.getSession());
        PlayerAware BtoBaction = constructionOperations.constructionResponse(B, B.getPlayerId(), playerA.getSession());

        assertEquals(AtoAaction, BtoAaction);
        assertEquals(AtoBaction, BtoBaction);
        assertTrue(AtoAaction instanceof InvitationAcceptedEvent);
        assertTrue(AtoBaction instanceof ExpectedAction);
    }

}
