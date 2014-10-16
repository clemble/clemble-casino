package com.clemble.casino.integration.game;

import static com.clemble.test.util.CollectionAssert.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.clemble.casino.game.event.GameEvent;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.lifecycle.management.outcome.PlayerWonOutcome;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.PlayerScenarios;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GameWatcherITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired
    public GameScenarios gameScenarios;

    @Test
    public void testSimpleScenarioObservation() {
        ClembleCasinoOperations player = playerScenarios.createPlayer();

        List<RoundGamePlayer> players = gameScenarios.round(Game.num);
        RoundGamePlayer A = players.get(0);
        RoundGamePlayer B = players.get(1);

        EventAccumulator<GameEvent> watcherListener = new EventAccumulator<GameEvent>();
        player.gameConstructionOperations().watch(A.getSessionKey(), watcherListener);

        assertTrue(A.isAlive());
        assertTrue(B.isAlive());

        A.perform(new SelectNumberAction(2));
        B.perform(new SelectNumberAction(1));

        A.waitForEnd();

        assertFalse(B.isAlive());
        assertFalse(A.isAlive());

        PlayerWonOutcome wonOutcome = (PlayerWonOutcome) B.getOutcome();
        assertEquals(wonOutcome.getPlayer(), A.playerOperations().getPlayer());

        assertTrue(watcherListener.toList().size() > 0);

        List<GameEvent> watchedEvents = watcherListener.toList();
        assertTrue(watchedEvents.size() > 0);

        assertContains(A.getEvents(), watchedEvents);
        assertContains(B.getEvents(), watchedEvents);
    }

}
